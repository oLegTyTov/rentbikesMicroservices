package com.example.commonresources.utils;

import com.example.commonresources.events.CreateUserEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class CreateUserEventDeserializer implements Deserializer<CreateUserEvent> {
    private ObjectMapper objectMapper=new ObjectMapper();


    @Override
    public CreateUserEvent deserialize(String topic, byte[] data) {
        try {
            if (data == null || data.length == 0) {
                return null;
            }
            return objectMapper.readValue(data, CreateUserEvent.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing CreateUserEvent", e);
        }
    }
}
