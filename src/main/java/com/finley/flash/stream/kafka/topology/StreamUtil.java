package com.finley.flash.stream.kafka.topology;

import com.finley.flash.stream.kafka.extractor.MiningTimestampExtractor;
import com.finley.flash.stream.kafka.serializer.JsonDeserializer;
import com.finley.flash.stream.kafka.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;


public class StreamUtil {

    public static <V> KStream<String, V> buildSourceKStream(StreamsBuilder builder, String topic, Class<V> valueClazz) {
        Consumed consumed = Consumed.with(Serdes.String(), jsonSerde(valueClazz), new MiningTimestampExtractor(), Topology.AutoOffsetReset.LATEST);
        KStream<String, V> source = builder.stream(topic, consumed);
        return source;
    }

    public static <T> Serde<T> jsonSerde(Class<T> clazz) {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(clazz));
    }
}
