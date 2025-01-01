package com.example.bikeusagemicroservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.bikeusagemicroservice.services.OperationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class OperationController {
    @Autowired
    private OperationService operationService;


}
