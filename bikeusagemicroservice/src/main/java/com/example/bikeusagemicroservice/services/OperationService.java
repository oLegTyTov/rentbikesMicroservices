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
import com.example.bikeusagemicroservice.entities.RentOperation;
import com.example.bikeusagemicroservice.entities.ReturnOperation;
import com.example.bikeusagemicroservice.repositories.RentOperationRepository;
import com.example.bikeusagemicroservice.repositories.ReturnOperationRepository;
import com.example.commonresources.events.BikeRentEvent;

@Service
public class OperationService {
    @Autowired
    private RentOperationRepository rentOperationRepository;
    @Autowired
    private ReturnOperationRepository returnOperationRepository;
    @Autowired
    private KafkaTemplate<String,BikeRentEvent>kafkaRentBike;
    @KafkaListener(topics = "successRentBikeUpdateStation",containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    //the final step of transaction rentBike
    public void addInformationRentBike(BikeRentEvent bikeRentEvent)
    {
        RentOperation operation=new RentOperation();
        operation.setBikeId(bikeRentEvent.getIdBike());
        operation.setTimestampBegin(LocalDateTime.now());
        operation.setIdSlot(bikeRentEvent.getIdSlot());
        operation.setTimestampEnd(LocalDateTime.now().plusHours(bikeRentEvent.getTimeRentHours().longValue()));
        operation.setUserId(bikeRentEvent.getIdUser());
    rentOperationRepository.save(operation);
    kafkaRentBike.send("successRentBikeTopic",bikeRentEvent);
    }
    @KafkaListener(topics = "stationChangedReturnBikeTopic", containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    public void addInformationTransactionReturnBike(BikeRentEvent bikeRentEvent)
    {
    ReturnOperation returnOperation=new ReturnOperation();
    returnOperation.setBikeId(bikeRentEvent.getIdBike());
    returnOperation.setTimestampReturnBike(LocalDateTime.now());
    returnOperation.setUserId(bikeRentEvent.getIdUser());
    returnOperation.setSlotId(bikeRentEvent.getIdSlot());
    returnOperationRepository.save(returnOperation);
    kafkaRentBike.send("successRentBikeTopic",bikeRentEvent);
    }
}