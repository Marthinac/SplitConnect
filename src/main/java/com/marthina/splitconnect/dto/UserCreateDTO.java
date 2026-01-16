package com.marthina.splitconnect.dto;

import com.marthina.splitconnect.model.enums.Country;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @NotBlank
    private String name;
    @NotNull
    private Country country;

    @NotBlank
    @Email(message = "Invalid email")
    private String email;

    @NotBlank
    @Size(min = 8)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Password must contain at least 8 chars, uppercase, lowercase, number, and special character."
    )
    private String password;
}