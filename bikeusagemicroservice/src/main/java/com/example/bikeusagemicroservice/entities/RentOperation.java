package com.example.bikeusagemicroservice.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RentOperation {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Long bikeId;
private Long userId;
private LocalDateTime timestampBegin;
private LocalDateTime timestampEnd;
private Long idSlot;
}
