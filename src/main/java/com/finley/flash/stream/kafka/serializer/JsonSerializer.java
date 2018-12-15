package com.finley.flash.stream.kafka.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSerializer<T> implements Serializer<T> {

    private static Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper jsonMapper;

    public JsonSerializer() {
        this.jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        try {
            return jsonMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            logger.error("serialize to json error|value={}", data, e);
            return null;
        }
    }

    @Override
    public void close() {
    }
}
