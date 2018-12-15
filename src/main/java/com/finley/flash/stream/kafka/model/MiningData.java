package com.finley.flash.stream.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MiningData {


    private Long userId;
    private BigDecimal share1Count;
    private Long createTime;

    public String getKey() {
        return this.userId.toString();
    }

    public MiningData() {
        this.share1Count = BigDecimal.ZERO;
    }

    public MiningData add(MiningData data, Long timeSpace) {
        if (data == null) {
            return this;
        }

        if (userId == null) {
            userId = data.getUserId();
        }

        if (createTime == null) {
            createTime = data.getCreateTime() / timeSpace * timeSpace + timeSpace;
        }

        this.share1Count = this.share1Count.add(data.getShare1Count());
        return this;
    }
}
