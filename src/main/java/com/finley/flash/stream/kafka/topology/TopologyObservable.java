package com.finley.flash.stream.kafka.topology;


public interface TopologyObservable {

    /**
     * add topology
     */
    void addTopology(TopologyObserver observer);
}
