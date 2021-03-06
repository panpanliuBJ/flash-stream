package com.finley.flash.stream.kafka.consumer;

import com.finley.flash.stream.consts.Beans;
import com.finley.flash.stream.consts.KafkaConsts;
import com.finley.flash.stream.consts.MetricsName;
import com.finley.flash.stream.domain.UserShareHour;
import com.finley.flash.stream.kafka.model.CoinInfo;
import com.finley.flash.stream.kafka.model.MiningData;
import com.finley.flash.stream.service.UserShareHourService;
import com.google.common.collect.Lists;
import io.micrometer.core.annotation.Timed;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;

@Slf4j
public class UserShareHourConsumer extends UserShareConsumer<UserShareHour> {

    @Autowired
    CoinInfo coinInfo;

    public UserShareHourConsumer(UserShareHourService userShareHourService) {
        super(log, userShareHourService);
    }

    @Override
    protected String getName() {
        return this.getClass().getSimpleName();
    }

    @Timed(percentiles = {0.9, 0.95, 0.99}, value = MetricsName.FLASH_USER_SHARE_HOUR_CONSUMED)
    @KafkaListener(topics = "${kafka.consumers.user.hour.topic}", groupId = KafkaConsts.USER_SHARE_GROUP, containerFactory = Beans.MINING_DATA_KAFKA_LISTENER_CONTAINER_FACTORY)
    public void consume(List<Message<MiningData>> data,
        Acknowledgment ack) {

        doWork(data, ack);
    }

    @Override
    protected List<UserShareHour> toUserShare(List<MiningData> miningDatas) {
        final ArrayList<UserShareHour> userShares = Lists.newArrayList();

        for (MiningData miningData : miningDatas) {
            final UserShareHour userSpeed = new UserShareHour();
            userSpeed.setUserId(miningData.getUserId());
            userSpeed.setShare(miningData.getShare1Count().longValue());
            userSpeed.setCreateTime(new Date(miningData.getCreateTime()));
            userShares.add(userSpeed);
        }
        return userShares;
    }
}
