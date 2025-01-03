package com.example.usermicroservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermicroservice.dtos.LoginRequest;
import com.example.usermicroservice.dtos.UserDto;
import com.example.usermicroservice.dtos.UserSecurity;
import com.example.usermicroservice.services.UserService;



@RestController
public class UserController {
    @Autowired
private UserService userService;


@PostMapping("/createUser")
public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
    userService.createUser(userDto);
    return ResponseEntity.ok().body("createUserSuccess");
}
@GetMapping("/checkValidToken")
public Boolean checkValidToken(@RequestParam String email) {
    userService.checkIsEmailValid(email);
        return true;
}
@PostMapping("/checkCredentials")
public ResponseEntity<Boolean> checkCredentials(@RequestBody LoginRequest loginRequest) {
    userService.checkCredentials(loginRequest);
    return ResponseEntity.ok().body(Boolean.TRUE);
}
@GetMapping("getUserSecurityByUsername/{username}")
public UserSecurity getUserSecurityByUserame(@PathVariable String username) {
    return userService.getUserSecurityByUsername(username);
}

}
