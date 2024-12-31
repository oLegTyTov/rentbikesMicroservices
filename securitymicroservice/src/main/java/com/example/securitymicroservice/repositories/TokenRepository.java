package com.example.securitymicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.securitymicroservice.entities.Token;
import java.util.List;


@Repository
public interface TokenRepository extends JpaRepository<Token,Long>{
    boolean existsByToken(String token);
    void deleteByToken(String token);
    Token findByToken(String token);
}
