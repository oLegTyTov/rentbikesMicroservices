package com.example.stationmicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stationmicroservice.entities.Station;

@Repository
public interface StationRepository extends JpaRepository<Station,Long>{
}
