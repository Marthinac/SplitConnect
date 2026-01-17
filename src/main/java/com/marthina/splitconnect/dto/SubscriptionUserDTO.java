package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.SubscriptionRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionUserDTO {
    private Long id;
    private Long userId;
    private Long subsId;

    private SubscriptionRole role;
    private LocalDateTime date;
}