package com.oisp.databackend.handlers.kafkaconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oisp.databackend.config.oisp.OispConfig;
import com.oisp.databackend.datasources.DataDao;
import com.oisp.databackend.datastructures.Observation;
import com.oisp.databackend.exceptions.ServiceException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ListenerExecutionFailedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final String maxpolls = "1000";
    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;
    @Autowired
    private OispConfig oispConfig;
    @Autowired
    //@Qualifier("dataDaoImpl")
    private DataDao dataDao;

    public KafkaConsumerProperties getKafkaConsumerProperties() {
        return kafkaConsumerProperties;
    }

    public void setKafkaConsumerProperties(KafkaConsumerProperties kafkaConsumerProperties) {
        this.kafkaConsumerProperties = kafkaConsumerProperties;
    }

    public OispConfig getOispConfig() {
        return oispConfig;
    }

    public void setOispConfig(OispConfig oispConfig) {
        this.oispConfig = oispConfig;
    }

    public DataDao getDataDao() {
        return dataDao;
    }

    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                oispConfig.getBackendConfig().getKafkaConfig().getUri());
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "backendConsumerGroup");
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
                oispConfig.getBackendConfig().getKafkaConfig().getMaxPayloadSize());
        props.put(
                ConsumerConfig.FETCH_MAX_BYTES_CONFIG,
                oispConfig.getBackendConfig().getKafkaConfig().getMaxPayloadSize());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxpolls);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer,
            String>> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);

        return factory;
    }

    @KafkaListener(topics = "#{kafkaConsumerProperties.getTopic()}")
    public void receive(List<String> rawObservationList) throws IOException, ServiceException {
        logger.debug("Start processing kafka samples batch " + rawObservationList.size());
        ObjectMapper mapper = new ObjectMapper();
        //String rawObservations = rawObservationList.get(0);
        List<Observation> observationList = new ArrayList<>();

        rawObservationList.forEach(rawObservation -> {
                Observation[] observations = null;
                if (rawObservation.trim().startsWith("[")) {
                    try {
                        observations = mapper.readValue(rawObservation, Observation[].class);
                    } catch (IllegalArgumentException | ListenerExecutionFailedException
                            | com.fasterxml.jackson.core.JsonProcessingException e) {
                        logger.warn("Tried to parse array. Will ignore the sample: " + e);
                    }
                } else {
                    try {
                        Observation observation = mapper.readValue(rawObservation, Observation.class);
                        observations = new Observation[]{observation};
                        if ("ByteArray".equals(observation.getDataType())) {
                            observation.setbValue(Base64.getDecoder().decode(observation.getValue()));
                            observation.setValue("0");
                        }
                    } catch (IllegalArgumentException | ListenerExecutionFailedException
                            | com.fasterxml.jackson.core.JsonProcessingException e) {
                        logger.warn("Tried to parse single observation. Will ignore the sample " + e);
                    }
                }
                if (observations != null) {
                    logger.debug("Received Observations in topic " + kafkaConsumerProperties.getTopic()
                            + ". Message: " + observations.toString());
                    observationList.addAll(Arrays.asList(observations));
                }
            });
        if (!dataDao.put(observationList.stream().toArray(Observation[]::new))) {
            throw new ServiceException("Data store error.");
        }
        logger.debug("End processing kafka sample");
    }
}