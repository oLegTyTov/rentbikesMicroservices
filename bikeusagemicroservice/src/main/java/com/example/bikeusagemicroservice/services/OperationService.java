package com.example.bikeusagemicroservice.services;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.bikeusagemicroservice.dtos.ResponseCheck;
import com.example.bikeusagemicroservice.entities.Operation;
import com.example.bikeusagemicroservice.entities.TypeOperation;
import com.example.bikeusagemicroservice.repositories.OperationRepository;
import com.example.commonresources.events.BikeRentEvent;

@Service
public class OperationService {
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private KafkaTemplate<String,BikeRentEvent>kafkaRentBike;
    @KafkaListener(topics = "successRentBikeUpdateStation",containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    //the final step of transaction rentBike
    public void addInformationRentBike(BikeRentEvent bikeRentEvent)
    {
        Operation operation=new Operation();
        operation.setBikeId(bikeRentEvent.getIdBike());
        operation.setTimestampBegin(LocalDateTime.now());
        operation.setTimestampEnd(LocalDateTime.now().plusHours(bikeRentEvent.getTimeRentHours().longValue()));
        operation.setUserId(bikeRentEvent.getIdUser());
        operation.setTypeOperation(TypeOperation.RENTAL);
    operationRepository.save(operation);
    kafkaRentBike.send("successRentBikeTopic",bikeRentEvent);
    }
}