package com.example.bikeusagemicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bikeusagemicroservice.entities.RentOperation;

@Repository
public interface RentOperationRepository extends JpaRepository<RentOperation,Long>{

}
