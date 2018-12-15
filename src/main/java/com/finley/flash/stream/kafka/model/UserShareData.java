package com.finley.flash.stream.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserShareData {

    @JsonProperty("nodeId")
    private Long nodeId;

    @JsonProperty("Data")
    private List<MiningData> data;
}
