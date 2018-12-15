package com.finley.flash.stream.kafka.topology;

import com.finley.flash.stream.configuration.KafkaProperties;
import com.finley.flash.stream.kafka.model.CoinInfo;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreamsStarter {

    @Autowired
    private CoinInfo coinInfo;

    @Autowired
    private KafkaProperties kafkaProperties;

    private KafkaStreams kafkaStreams;

    public StreamsStarter(KafkaProperties kafkaProperties, CoinInfo coinInfo) {
        this.kafkaProperties = kafkaProperties;
        this.coinInfo = coinInfo;
    }


    @PostConstruct
    public void startUp() {
        final StreamsBuilder builder = new StreamsBuilder();
        buildTopology(builder);
        final Topology topology = builder.build();
        log.info("topology={}", topology.describe());
        kafkaStreams = new KafkaStreams(topology, kafkaProperties.getStreamProperties());
        run();
    }

    private void buildTopology(StreamsBuilder builder) {

        UserShareStream userShareStream = new UserShareStream(kafkaProperties, coinInfo);
        userShareStream.buildTopology(builder);
    }

    private void run() {
        try {
            kafkaStreams.start();
        } catch (Exception ex) {
            log.error("kafkaStreams start error", ex);
        }
    }

    @PreDestroy
    public void shutDown() {
        kafkaStreams.close();
    }
}
