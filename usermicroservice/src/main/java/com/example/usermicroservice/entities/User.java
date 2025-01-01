package com.example.usermicroservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.example.usermicroservice.dtos.UserDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(unique = true)
    private String email;
    private String name;
    private String surname;
    private LocalDateTime registrationDate;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserState userState;
    private Double balance;
    private Boolean isInTransaction;
    public User(UserDto userDto) {
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
        this.email = userDto.getEmail();
        this.name = userDto.getName();
        this.surname = userDto.getSurname();
        this.phoneNumber = userDto.getPhoneNumber();
        this.registrationDate = LocalDateTime.now();
        this.userState = UserState.INPROCESS; 
        this.balance=userDto.getBalance();
        this.isInTransaction=false;
    }
}
