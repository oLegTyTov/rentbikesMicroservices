package com.example.commonresources.events;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BikeRentEvent {
    private Long idUser;
    private Long idBike;
    private Integer timeRentHours;
    private Long idSlot;
}
