package com.finley.flash.stream.kafka.extractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.finley.flash.stream.kafka.model.MiningData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

@Slf4j
public class MiningTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {
        Object value = record.value();
        if (value instanceof MiningData) {
            return ((MiningData) value).getCreateTime();
        }

        if (value instanceof JsonNode) {
            return ((JsonNode) record.value()).get("createTime").longValue();
        }
        return record.timestamp();
    }
}
