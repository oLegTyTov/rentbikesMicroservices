package com.example.stationmicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.stationmicroservice.entities.Slot;
import java.util.List;
import java.util.Optional;


@Repository
public interface SlotRepository extends JpaRepository<Slot,Long>{
 Optional<Slot> findByBikeId(Long bikeId);
}
