package com.example.bikemicroservice.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.bikemicroservice.entities.Bike;
import com.example.bikemicroservice.entities.BikeStatus;
import com.example.bikemicroservice.exceptions.BikeNotFoundException;
import com.example.bikemicroservice.repositories.BikeRepository;
import com.example.commonresources.events.BikeRentEvent;

@Service
public class BikeService {
    @Autowired
    private BikeRepository bikeRepository;
    @Autowired
    private KafkaTemplate<String, BikeRentEvent> kafkaTemplate;

    // begining of saga choreography pattern
    public void changeBikeTransactionRentBike(BikeRentEvent bikeRentEvent) {
        try {
            System.out.println(bikeRentEvent.getIdBike());
            Bike bike = bikeRepository.findById(bikeRentEvent.getIdBike()).orElseThrow(() -> new BikeNotFoundException());
            if (bike.getBikeStatus() != BikeStatus.AVAILABLE || bike.getIsInTransaction()) {
                throw new BikeNotFoundException();
            } else {
                bike.setBikeStatus(BikeStatus.INUSE);
                bikeRentEvent.setIdSlot(bike.getIdSlot());
                bike.setIdSlot(null);
                bike.setIsInTransaction(true);
                bikeRepository.save(bike);
                kafkaTemplate.send("successBikeChangedStatusTopic", bikeRentEvent);
            }
        } catch (BikeNotFoundException e) {
            System.out.println("errorTrnasactionRentBike");
        }
    }

    @KafkaListener(topics = "rollbackCheckUserBalanceTopic", containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    public void rollBackRentBike(BikeRentEvent bikeRentEvent) {
        Bike bike = bikeRepository.findById(bikeRentEvent.getIdBike()).get();
        bike.setBikeStatus(BikeStatus.AVAILABLE);
        bike.setIsInTransaction(false);
        bike.setIdSlot(bikeRentEvent.getIdSlot());//todo we must change event and add idSlot
        bikeRepository.save(bike);
    }

    @KafkaListener(topics = "successRentBikeTopic", containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    public void successRentBikeTopic(BikeRentEvent bikeRentEvent) {
        Bike bike = bikeRepository.findById(bikeRentEvent.getIdBike()).get();
        bike.setIsInTransaction(false);
        bikeRepository.save(bike);
    }

    public void changeBikeTransactionReturnBike(BikeRentEvent bikeRentEvent) {
        try{
        Bike bike=bikeRepository.findById(bikeRentEvent.getIdBike()).orElseThrow(()->new BikeNotFoundException());
        if(bike.getBikeStatus()==BikeStatus.INUSE && !bike.getIsInTransaction())
        {
        bike.setBikeStatus(BikeStatus.AVAILABLE);
        bike.setIdSlot(bikeRentEvent.getIdSlot());
        bike.setIsInTransaction(true);
        bikeRepository.save(bike);
        kafkaTemplate.send("changedBikeReturnBikeTopic",bikeRentEvent);
        }
        }
        catch(BikeNotFoundException e)
        {
        System.out.println("BikeNotFoundException");
        }
    }
    @KafkaListener(topics = "stationRollabackReturnBikeTopic",containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    public void rollBackBikeChangeTransactionReturnBike(BikeRentEvent bikeRentEvent)
    {
    Bike bike=bikeRepository.findById(bikeRentEvent.getIdBike()).get();
    bike.setBikeStatus(BikeStatus.INUSE);
    bike.setIdSlot(null);
    bike.setIsInTransaction(false);
    bikeRepository.save(bike);
    }
}
