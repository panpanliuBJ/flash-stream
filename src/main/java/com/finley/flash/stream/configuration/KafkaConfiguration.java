package com.finley.flash.stream.configuration;

import com.finley.flash.stream.consts.Beans;
import com.finley.flash.stream.kafka.consumer.UserShareHourConsumer;
import com.finley.flash.stream.kafka.consumer.UserShareMin15Consumer;
import com.finley.flash.stream.kafka.consumer.UserShareMin5Consumer;
import com.finley.flash.stream.kafka.model.MiningData;
import com.finley.flash.stream.service.UserShareHourService;
import com.finley.flash.stream.service.UserShareMin15Service;
import com.finley.flash.stream.service.UserShareMin5Service;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@Configuration
public class KafkaConfiguration {

    @Value("${kafka.stream-config.bootstrap.servers}")
    private String bootstrapServers;


    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "stream-consumer");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return props;
    }

    @Bean(Beans.MINING_DATA_KAFKA_LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, MiningData> userKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MiningData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(MiningData.class));
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private <T> ConsumerFactory<String, T> consumerFactory(Class<T> clazz) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
            new JsonDeserializer<>(clazz));
    }

    @Bean
    public UserShareMin5Consumer min5UserConsumer(UserShareMin5Service userShareMin5Service) {
        return new UserShareMin5Consumer(userShareMin5Service);
    }

    @Bean
    public UserShareMin15Consumer min15UserConsumer(UserShareMin15Service userShareMin15Service) {
        return new UserShareMin15Consumer(userShareMin15Service);
    }

    @Bean
    public UserShareHourConsumer hourUserConsumer(UserShareHourService userShareHourService) {
        return new UserShareHourConsumer(userShareHourService);
    }

}
