package com.example.usermicroservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private Double balance;
    
}
