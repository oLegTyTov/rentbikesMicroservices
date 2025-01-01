package com.example.commonresources.utils;
import com.example.commonresources.events.CreateUserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
public class CreateUserEventSerializer implements Serializer<CreateUserEvent> {
    private ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public byte[] serialize(String topic, CreateUserEvent data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing CreateUserEvent", e);
        }
    }
}

