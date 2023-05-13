package com.ossovita.kafka.config;

import org.springframework.kafka.support.serializer.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
public class JsonSerializerWithJTM <T> extends JsonSerializer<T> {

    public JsonSerializerWithJTM() {
        super();
        objectMapper.registerModule(new JavaTimeModule());
        //whatever you want to configure here
    }
    public JsonSerializerWithJTM(ObjectMapper objectMapper) {
        super(objectMapper);
    }

}