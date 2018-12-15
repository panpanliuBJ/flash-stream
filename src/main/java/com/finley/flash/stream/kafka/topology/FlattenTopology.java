package com.finley.flash.stream.kafka.topology;

import com.finley.flash.stream.kafka.model.MiningData;
import com.finley.flash.stream.kafka.model.MiningTopologyMeta;
import com.finley.flash.stream.kafka.model.UserShareData;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

/**
 * flatten UserShareData
 */

@Slf4j
public class FlattenTopology implements TopologyObservable {

    protected final MiningTopologyMeta meta;
    private List<TopologyObserver> topoObservers;
    private KStream<String, MiningData> stream;


    public FlattenTopology(MiningTopologyMeta meta) {
        this.meta = meta;
        topoObservers = Lists.newArrayList();
    }

    @Override
    public void addTopology(TopologyObserver observer) {
        topoObservers.add(observer);
    }

    public void buildTopology(StreamsBuilder builder) {
        log.info("build {} topology ...", this.getClass().getSimpleName());
        KStream<String, UserShareData> source = StreamUtil.buildSourceKStream(builder, meta.getSourceTopic(), UserShareData.class);

        source.filter((k, v) -> v != null)
            .flatMapValues(value -> value.getData())
            .filter((k, v) -> v != null)
            .to(meta.getToTopic(), Produced.with(Serdes.String(), StreamUtil.jsonSerde(MiningData.class)));

        stream = StreamUtil.buildSourceKStream(builder, meta.getToTopic(), MiningData.class);
    }

    public void notifyAll(StreamsBuilder builder) {
        for (TopologyObserver observer : topoObservers) {
            observer.notify(builder, stream);
        }
    }
}
