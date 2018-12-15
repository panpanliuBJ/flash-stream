package com.finley.flash.stream.kafka.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CoinInfo {

    private String coinType;

    @Value("${coin.type:btc}")
    public void setCoinType(String coin) {
        this.coinType = coin.toLowerCase();
    }
}
