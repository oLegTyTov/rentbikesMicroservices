package com.example.stationmicroservice.entities;

import java.util.List;

import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Station {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
private String location;
private Long availableSlots;
private Long occupiedSlots;
@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "station") // Додано mappedBy
private List<Slot>slots;
}
