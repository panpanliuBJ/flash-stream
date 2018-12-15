package com.finley.flash.stream.kafka.topology;

import com.finley.flash.stream.configuration.KafkaProperties;
import com.finley.flash.stream.kafka.model.CoinInfo;
import com.finley.flash.stream.kafka.model.GranularityType;
import com.finley.flash.stream.kafka.model.MiningTopologyMeta;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;


@Slf4j
public class UserShareStream {

    private CoinInfo coinInfo;

    private KafkaProperties kafkaProperties;

    UserShareStream(KafkaProperties kafkaProperties, CoinInfo coinInfo) {
        this.coinInfo = coinInfo;
        this.kafkaProperties = kafkaProperties;
    }

    void buildTopology(StreamsBuilder builder) {

        final String flattenTopic = String.format("%s-flatten-share", coinInfo.getCoinType());

        try {
            final MiningTopologyMeta flattenMeta = MiningTopologyMeta.builder()
                .sourceTopic(kafkaProperties.getTopic())
                .toTopic(flattenTopic)
                .build();
            final FlattenTopology flattenTopology = new FlattenTopology(flattenMeta);
            flattenTopology.buildTopology(builder);

            final MiningTopologyMeta user5MinutesMeta = MiningTopologyMeta.builder()
                .sourceTopic(flattenMeta.getToTopic())
                .timeUnit(TimeUnit.MINUTES)
                .timeValue(5)
                .coinType(coinInfo.getCoinType())
                .granularityType(GranularityType.MIN5)
                .build();
            final UserSubTopology user5MinSubTopology = new UserSubTopology(user5MinutesMeta);
            flattenTopology.addTopology(user5MinSubTopology);

            final MiningTopologyMeta user15MinutesMeta = MiningTopologyMeta.builder()
                .sourceTopic(flattenMeta.getToTopic())
                .timeUnit(TimeUnit.MINUTES)
                .timeValue(15)
                .coinType(coinInfo.getCoinType())
                .granularityType(GranularityType.MIN15)
                .build();
            final UserSubTopology user15MinSubTopology = new UserSubTopology(user15MinutesMeta);
            flattenTopology.addTopology(user15MinSubTopology);

            final MiningTopologyMeta userHoursMeta = MiningTopologyMeta.builder()
                .sourceTopic(flattenMeta.getToTopic())
                .timeUnit(TimeUnit.HOURS)
                .timeValue(1)
                .coinType(coinInfo.getCoinType())
                .granularityType(GranularityType.HOUR)
                .build();
            final UserSubTopology userHoursSubTopology = new UserSubTopology(userHoursMeta);
            flattenTopology.addTopology(userHoursSubTopology);

            flattenTopology.notifyAll(builder);

        } catch (Exception ex) {
            log.error("fail to build topology|exception:", ex);
        }
    }
}
