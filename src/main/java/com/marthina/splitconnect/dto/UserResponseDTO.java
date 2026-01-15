package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.Country;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String name;
    private Country country;
    private String email;

}
