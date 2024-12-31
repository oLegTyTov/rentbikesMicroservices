package com.example.stationmicroservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.commonresources.events.BikeRentEvent;
import com.example.stationmicroservice.entities.Slot;
import com.example.stationmicroservice.entities.Station;
import com.example.stationmicroservice.entities.StatusSlot;
import com.example.stationmicroservice.exceptions.SlotNotFoundException;
import com.example.stationmicroservice.repositories.SlotRepository;
import com.example.stationmicroservice.repositories.StationRepository;

@Service
public class StationService {
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private KafkaTemplate<String, BikeRentEvent> kafkaTemplateRentBike;

    @KafkaListener(topics = "successCheckUserBalanceTopic",containerFactory = "kafkaListenerContainerFactoryBikeRentEvent")
    public void rentBikeUpdateStation(BikeRentEvent bikeRentEvent) {
        try {
            System.out.println(bikeRentEvent);
            Slot slot = slotRepository.findByBikeId(bikeRentEvent.getIdBike())
                    .orElseThrow(() -> new SlotNotFoundException());
            slot.setStatusSlot(StatusSlot.AVAILABLE);
            slot.setBikeId(null);
            slotRepository.save(slot);
            slotRepository.flush();
            Station station = slot.getStation();
            station.setOccupiedSlots(station.getOccupiedSlots() - 1);
            station.setAvailableSlots(station.getAvailableSlots() + 1);
            stationRepository.save(station);
            stationRepository.flush();
            kafkaTemplateRentBike.send("successRentBikeUpdateStation",bikeRentEvent);
        } catch (SlotNotFoundException e) {
            kafkaTemplateRentBike.send("rollbackRentBikeUpdateStation", bikeRentEvent);
        }
    }
}
