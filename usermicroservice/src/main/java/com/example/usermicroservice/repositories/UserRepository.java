package com.example.usermicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.usermicroservice.entities.User;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long>{
boolean existsByEmail(String email);
boolean existsByUsername(String username);
Optional<User> findByEmail(String email);
Optional<User> findByUsername(String username);
}
