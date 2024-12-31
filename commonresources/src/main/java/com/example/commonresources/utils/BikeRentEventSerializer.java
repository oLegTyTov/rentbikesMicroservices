package com.example.commonresources.utils;



import com.example.commonresources.events.BikeRentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
public class BikeRentEventSerializer implements Serializer<BikeRentEvent> {
    private ObjectMapper objectMapper=new ObjectMapper();


   

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Конфігурація, якщо необхідно
    }

    @Override
    public byte[] serialize(String topic, BikeRentEvent data) {
        System.out.println("Serialize BikeRentEvent");
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing BikeRentEvent", e);
        }
    }

    @Override
    public void close() {
        // Закриття ресурсів, якщо потрібно
    }
}
