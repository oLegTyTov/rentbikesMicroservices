package com.example.bikemicroservice.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.bikemicroservice.entities.Bike;
import com.example.bikemicroservice.entities.BikeStatus;
import com.example.bikemicroservice.exceptions.BikeException;
import com.example.bikemicroservice.repositories.BikeRepository;
import com.example.commonresources.events.BikeRentEvent;

@Service
public class BikeService {
    @Autowired
    private BikeRepository bikeRepository;
    @Autowired
    private KafkaTemplate<String, BikeRentEvent> kafkaTemplate;

    // begining of saga choreography pattern
    public void doRentBike(BikeRentEvent bikeRentEvent) {
        try {
            System.out.println(bikeRentEvent.getIdBike());
            Bike bike = bikeRepository.findById(bikeRentEvent.getIdBike()).orElseThrow(() -> new BikeException());
            if (bike.getBikeStatus() != BikeStatus.AVAILABLE || bike.getIsInTransaction()) {
                throw new BikeException();
            } else {
                bike.setBikeStatus(BikeStatus.INUSE);
                bikeRentEvent.setIdSlot(bike.getIdSlot());
                bike.setIdSlot(null);
                bike.setIsInTransaction(true);
                bikeRepository.save(bike);
                kafkaTemplate.send("successBikeChangedStatusTopic", bikeRentEvent);
            }
        } catch (BikeException e) {
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
}
