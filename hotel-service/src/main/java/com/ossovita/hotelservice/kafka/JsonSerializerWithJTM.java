package com.ossovita.hotelservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.support.serializer.JsonSerializer;
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