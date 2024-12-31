package com.example.bikemicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bikemicroservice.entities.Bike;
@Repository
public interface BikeRepository extends JpaRepository<Bike,Long>{

}
