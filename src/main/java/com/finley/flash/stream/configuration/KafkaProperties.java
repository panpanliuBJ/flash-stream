package com.finley.flash.stream.configuration;

import com.finley.flash.stream.kafka.extractor.MiningTimestampExtractor;
import java.util.Map;
import java.util.Properties;
import lombok.Data;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties("kafka")
@Data
public class KafkaProperties {

    private String topic;

    private Map<String, String> streamConfig;

    public Properties getStreamProperties() {
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : streamConfig.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }

        properties
            .put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, MiningTimestampExtractor.class.getName());
        return properties;
    }


}
