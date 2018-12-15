package com.finley.flash.stream.kafka.topology;

import com.finley.flash.stream.kafka.model.MiningData;
import com.finley.flash.stream.kafka.model.MiningTopologyMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.WindowStore;


@Slf4j
public class UserSubTopology extends AbstractTopology<MiningData> implements
    TopologyObserver<MiningData> {

    public static final String USER = "user";

    public UserSubTopology(MiningTopologyMeta meta) {
        super(meta, MiningData.class);
    }

    @Override
    public void notify(StreamsBuilder builder, KStream<String, MiningData> stream) {
        aggregateToTopic(stream, USER);
    }

    @Override
    public KStream<Windowed<String>, MiningData> doAggregate(KStream<String, MiningData> source, String storeName, String toTopic) {
        long windowSizeMs = getWindowSizeMs();
        //已经是groupBy之后的结果
        KStream<Windowed<String>, MiningData> summaryKStream = source.filter((k, v) -> v != null)
            .map((k, v) -> new KeyValue<>(v.getKey(), v))
            .groupBy((k, v) -> k, Serialized.with(Serdes.String(), StreamUtil.jsonSerde(MiningData.class)))
            .windowedBy(TimeWindows.of(windowSizeMs).advanceBy(windowSizeMs))
            .aggregate(() -> new MiningData(),
                (aggKey, newValue, aggValue) -> aggValue.add(newValue, toMillis(meta.getTimeUnit(), meta.getTimeValue())),
                Materialized.<String, MiningData, WindowStore<Bytes, byte[]>>as(storeName).withValueSerde(StreamUtil.jsonSerde(MiningData.class)))
            .toStream();
        return summaryKStream;
    }

    @Override
    public void postAggregate(KStream<Windowed<String>, MiningData> stream, String toTopic) {
        stream.foreach((key, value) -> log.info("toTopic={}|key={}|value={}", toTopic, key, value));
    }

}




