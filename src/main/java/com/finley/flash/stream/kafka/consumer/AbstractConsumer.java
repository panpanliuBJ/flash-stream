package com.finley.flash.stream.kafka.consumer;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;

@Slf4j
public abstract class AbstractConsumer<T> {

    /**
     * consumer name
     */
    protected abstract String getName();

    /**
     * 处理数据
     */
    public void doWork(List<Message<T>> data, Acknowledgment ack) {
        if (CollectionUtils.isEmpty(data)) {
            log.warn("no data fetched|consumer name:{}|partition size:{}", getName(), data.size());
            return;
        }

        Object startOffset = data.get(0).getHeaders().get(KafkaHeaders.OFFSET);
        Object endOffset = data.get(data.size() - 1).getHeaders().get(KafkaHeaders.OFFSET);
        log.info("consumer name:{}|partition size:{}|offset min:{}|offset max:{}|data size:{}", getName(), data.size(), startOffset, endOffset, data.size());

        try {
            List<T> collect = data.stream().map(Message::getPayload).collect(Collectors.toList());
            sinkMessage(collect);
            ack.acknowledge();
        } catch (Exception ex) {
            log.error("fail to consume {}|exception:", getName(), ex);
        }
    }

    /**
     * 处理消息
     */
    public abstract boolean sinkMessage(List<T> miningDatas);
}
