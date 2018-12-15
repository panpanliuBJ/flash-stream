package com.finley.flash.stream.kafka.serializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDeserializer<T> implements Deserializer<T> {
    private static Logger logger = LoggerFactory.getLogger(JsonDeserializer.class);

    private ObjectMapper jsonMapper;

    private Class<T> deserializerClass;

    /**
     * Default constructor needed by Kafka
     */
    public JsonDeserializer() {
        this.jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JsonDeserializer(Class<T> deserializerClass) {
        this.deserializerClass = deserializerClass;
        this.jsonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        if (deserializerClass == null) {
            deserializerClass = (Class<T>) map.get("serializedClass");
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return jsonMapper.readValue(bytes, deserializerClass);
        } catch (IOException e) {
            logger.error("deserialize from json error|value={}", new String(bytes), e);
            return null;
        }
    }

    @Override
    public void close() {
    }
}
