package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.Country;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String name;
    private Country country;
    //todo tirar no fim do projeto depois dos testes, pois é dado sensível
    private String email;

}
