package com.marthina.splitconnect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeEmailDTO {
    @NotBlank
    private String newEmail;
}
