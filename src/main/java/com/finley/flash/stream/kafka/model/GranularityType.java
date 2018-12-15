package com.finley.flash.stream.kafka.model;

import lombok.Getter;


public enum GranularityType {
    MIN5("min5"),
    MIN15("min15"),
    HOUR("hour");


    @Getter
    private String type;

    GranularityType(String type) {
        this.type = type;
    }
}
