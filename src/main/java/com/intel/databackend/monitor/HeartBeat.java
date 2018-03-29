package com.intel.databackend.handlers;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import com.intel.databackend.config.ServiceConfigProvider;
import com.intel.databackend.exceptions.VcapEnvironmentException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.admin.RackAwareMode.Safe$;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import kafka.utils.ZkUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PreDestroy;
import java.util.Properties;


@Component
public class HeartBeat implements ApplicationListener<ApplicationReadyEvent>, Runnable { 

    private static final Logger logger = LoggerFactory.getLogger(Data.class);

    private KafkaProducer<String, String> kafkaProducer;

    private String topic;    

    @Autowired
    private ServiceConfigProvider serviceConfigProvider;


    @Autowired
    public HeartBeat(KafkaProducer<String, String> kafkaProducer) {
        logger.error("================= HeartBeat");
        this.kafkaProducer = kafkaProducer;
    }


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        ThreadPoolTaskScheduler s = new ThreadPoolTaskScheduler();
        s.setThreadNamePrefix("HeatBeat");
        s.initialize();
        logger.error("================= Ready1");

        createTopic();
 
        logger.error("================= Ready2");

        try {
            Integer period = serviceConfigProvider.getKafkaHeartbeatInterval();
            s.scheduleAtFixedRate(this, period);
        } catch (VcapEnvironmentException e) {
            logger.error("Kafka configuration is not available.", e);
        }

    }
 
    @Override
    public void run() {
        if (kafkaProducer != null) {
            ProducerRecord<String, String> message = new ProducerRecord<String, String>(topic, "backend");
            kafkaProducer.send(message);
        }
    }

    private void createTopic()  {
        if (kafkaProducer != null) {
            ZkClient zkClient = null;
            String brokerURI = null;
            ZkUtils zkUtils = null;
            try {
                topic = serviceConfigProvider.getKafkaHeartbeatTopicName();
                Integer partitions = serviceConfigProvider.getKafkaPartitionsFactor();
                Integer replicationFactor = serviceConfigProvider.getKafkaReplicationFactor();
                Integer timeoutInMs = serviceConfigProvider.getKafkaTimeoutInMs();
                brokerURI = serviceConfigProvider.getZookeeperUri();
                zkClient = new ZkClient(brokerURI, timeoutInMs, timeoutInMs, ZKStringSerializer$.MODULE$);
                boolean isSecureKafkaCluster = false;
                zkUtils = new ZkUtils(zkClient, new ZkConnection(brokerURI), isSecureKafkaCluster);

                if (!AdminUtils.topicExists(zkUtils, topic)) {
                    logger.error("Topic: {} does not exist. Creating...", topic);
                    RackAwareMode rackAwareMode = Safe$.MODULE$;
                    AdminUtils.createTopic(zkUtils, topic, partitions, replicationFactor, new Properties(), rackAwareMode);
                } else {
                    logger.info("Topic: {} exist and will be use for pushing messages", topic);
                }
            } catch (ZkException | VcapEnvironmentException e) {
                logger.error("error during topic creation! Topic: {}, Broker URI: {}. KafkaSenderService will be unavailable!",
                        topic, brokerURI, e);
                kafkaProducer = null;
            } finally {
                if (zkClient != null) {
                    zkClient.close();
                }
            }
        }
    }

    @PreDestroy
    protected void close() {
        if (kafkaProducer != null) {
            kafkaProducer.close();
        }
    }

} 