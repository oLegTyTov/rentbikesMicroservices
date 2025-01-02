package com.example.bikeusagemicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bikeusagemicroservice.entities.ReturnOperation;
@Repository
public interface ReturnOperationRepository extends JpaRepository<ReturnOperation,Long>{

}
