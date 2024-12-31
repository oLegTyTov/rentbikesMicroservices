package com.example.commonresources.utils;

import com.example.commonresources.events.BikeRentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

public class BikeRentEventDeserializer implements Deserializer<BikeRentEvent> {
    private ObjectMapper objectMapper=new ObjectMapper();


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Конфігурація, якщо необхідно
    }

    @Override
    public BikeRentEvent deserialize(String topic, byte[] data) {
        System.out.println("Deserialize BikeRentEvent");
        try {
            if (data == null || data.length == 0) {
                return null;
            }
            return objectMapper.readValue(data, BikeRentEvent.class);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing BikeRentDTO", e);
        }
    }

    @Override
    public void close() {
        // Закриття ресурсів, якщо потрібно
    }
}
