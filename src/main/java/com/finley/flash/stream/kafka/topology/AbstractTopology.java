package com.finley.flash.stream.kafka.topology;

import com.finley.flash.stream.kafka.model.MiningTopologyMeta;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindowedDeserializer;
import org.apache.kafka.streams.kstream.TimeWindowedSerializer;
import org.apache.kafka.streams.kstream.Windowed;


public abstract class AbstractTopology<T> {

    protected final MiningTopologyMeta meta;
    protected Class<T> clazz;

    public AbstractTopology(MiningTopologyMeta meta, Class<T> clazz) {
        this.meta = meta;
        this.clazz = clazz;
    }

    public abstract KStream<Windowed<String>, T> doAggregate(KStream<String, T> source, String storeName, String toTopic);


    protected String aggregateToTopic(KStream<String, T> source, String key) {

        Serde<Windowed<String>> windowedSerde = Serdes.serdeFrom(new TimeWindowedSerializer(new StringSerializer()), new TimeWindowedDeserializer(new StringDeserializer()));
        //user-share-store-minutes
        String storeName = String.format("%s-share-store-%s", key, meta.getGranularityType().getType());
        String toTopic = String.format("%s-%s-share-%s", meta.getCoinType(), key, meta.getGranularityType().getType());

        KStream<Windowed<String>, T> summaryKStream = doAggregate(source, storeName, toTopic);
        summaryKStream.to(toTopic, Produced.with(windowedSerde, StreamUtil.jsonSerde(this.clazz)));
        postAggregate(summaryKStream, toTopic);
        return toTopic;
    }

    protected long getWindowSizeMs() {
        return meta.getTimeUnit().toMillis(meta.getTimeValue());
    }

    /**
     * post handler after aggregate
     */
    public abstract void postAggregate(KStream<Windowed<String>, T> stream, String toTopic);


    protected Long toMillis(TimeUnit timeUnit, long timeValue) {
        return timeUnit.toMillis(timeValue);
    }
}
