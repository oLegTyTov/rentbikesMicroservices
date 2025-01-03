package com.example.bikemicroservice.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.bikemicroservice.dtos.UserSecurity;
import com.example.bikemicroservice.services.BikeService;
import com.example.commonresources.events.BikeRentEvent;
@RestController
public class BikeController {
    @Autowired
    private BikeService bikeService;
@PostMapping("/doRentBike")
public String doRentBike(@RequestParam Long idBike,@RequestParam Integer timeRentHours,@AuthenticationPrincipal UserSecurity userSecurity) {
    bikeService.changeBikeTransactionRentBike(new BikeRentEvent(userSecurity.getIdUser(),idBike, timeRentHours,null));
    return "doRentBike Transaction started";
}
@PostMapping("/returnBike")
public String returnBike(@RequestParam Long idBike,@AuthenticationPrincipal UserSecurity userSecurity,@RequestParam Long idSlot) {    
    bikeService.changeBikeTransactionReturnBike(new BikeRentEvent(userSecurity.getIdUser(),idBike,null,idSlot));
    return "returnBike Transaction started";
}

}
