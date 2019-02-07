package com.oisp.databackend.monitor;

import com.oisp.databackend.config.oisp.JaegerConfig;
import com.oisp.databackend.config.oisp.OispConfig;
import com.oisp.databackend.datasources.DataDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaegerTracing {

    private static final Logger logger = LoggerFactory.getLogger(DataDaoImpl.class);

    @Autowired
    private OispConfig oispConfig;

    @Bean
    public io.opentracing.Tracer jaegerTracer() {
        JaegerConfig config = oispConfig.getBackendConfig().getJaegerConfig();

        //Should we allow tracing?
        String serviceName = "dummy";
        if (config != null) {
            serviceName = config.getServiceName();
        }
        if (config == null || !config.isTracing()) {
            return new io.jaegertracing.Configuration(serviceName).getTracer();
        }
        //check whether Kubernetes or Docker
        //For Kubernetes, select "localhost" as Jaeger collector
        //For Docker, select the given service name

        String agentHost = "localhost";
        String kubernetesPort = System.getenv("KUBERNETES_PORT");
        if (kubernetesPort == null) {
            agentHost = config.getAgentHost();
        }

        io.jaegertracing.Configuration.SamplerConfiguration samplerConfiguration = new io.jaegertracing.Configuration.SamplerConfiguration();
        samplerConfiguration.withParam(config.getSamplerParam()).withType(config.getSamplerType());

        io.jaegertracing.Configuration.SenderConfiguration senderConfiguration = new io.jaegertracing.Configuration.SenderConfiguration();
        senderConfiguration.withAgentHost(agentHost).withAgentPort(config.getAgentPort());

        io.jaegertracing.Configuration.ReporterConfiguration reporterConfiguration = new io.jaegertracing.Configuration.ReporterConfiguration();
        reporterConfiguration.withLogSpans(config.isLogSpans()).withSender(senderConfiguration);

        logger.info("Initializing tracer to host " + agentHost);
        return new io.jaegertracing.Configuration(config.getServiceName())
                .withSampler(samplerConfiguration)
                .withReporter(reporterConfiguration)
                .getTracer();
    }
}
