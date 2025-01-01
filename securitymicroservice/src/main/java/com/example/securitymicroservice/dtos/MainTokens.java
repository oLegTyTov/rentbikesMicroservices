package com.example.securitymicroservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainTokens {
private String refreshToken;
private String accessToken;
private Boolean success;
public MainTokens(String refreshToken,String accessToken)
{
this.accessToken=accessToken;
this.refreshToken=refreshToken;
}
}
