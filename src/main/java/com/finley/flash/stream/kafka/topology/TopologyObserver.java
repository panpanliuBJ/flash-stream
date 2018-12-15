package com.finley.flash.stream.kafka.topology;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

public interface TopologyObserver<T> {

    /**
     * notify stream
     */
    void notify(StreamsBuilder builder, KStream<String, T> stream);
}
