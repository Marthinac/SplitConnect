package com.marthina.splitconnect.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionUserDTO {
    private Long id;
    private Long userId;
    private Long subsId;

    private String role;
    private LocalDate date;
}