package com.example.bikeusagemicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bikeusagemicroservice.entities.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation,Long>{

}
