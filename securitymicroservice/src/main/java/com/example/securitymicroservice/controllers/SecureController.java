package com.example.securitymicroservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitymicroservice.dtos.LoginRequest;
import com.example.securitymicroservice.dtos.MainTokens;
import com.example.securitymicroservice.entities.Token;
import com.example.securitymicroservice.services.TokenService;
import com.example.securitymicroservice.utils.JwtUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class SecureController {
    @Autowired
private TokenService tokenService;

@GetMapping("/getverificationtoken")
public ResponseEntity<String> getverificationtoken(@RequestParam String email) {
return ResponseEntity.ok(tokenService.getVerificationToken(email));
}
@GetMapping("/doVerificationAccount/{token}")
public ResponseEntity<String> doVerificationAccount(@PathVariable String token) {
    tokenService.doVerificationAccount(token);
    return ResponseEntity.ok("GooddoVerificationAccount");
}
@PostMapping("/login")
public MainTokens login(@RequestBody LoginRequest loginRequest) {
    return tokenService.login(loginRequest);
}
@GetMapping("/getUsernameByAccessToken/{accessToken}")
public String getEmailByAccessToken(@PathVariable String accessToken) {
    return tokenService.getUsernameByAccessToken(accessToken);
}



}
