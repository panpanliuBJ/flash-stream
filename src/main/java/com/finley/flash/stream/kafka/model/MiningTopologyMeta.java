package com.finley.flash.stream.kafka.model;

import java.util.concurrent.TimeUnit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MiningTopologyMeta {

    private String sourceTopic;

    private String toTopic;

    private TimeUnit timeUnit;

    private long timeValue;

    private Class outputClass;

    private String coinType;

    private GranularityType granularityType;

}
