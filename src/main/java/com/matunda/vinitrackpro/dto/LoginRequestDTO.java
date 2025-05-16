package com.matunda.vinitrackpro.dto;
import lombok.Data;
import jakarta.validation.constraints.*;
@Data
public class LoginRequestDTO {

    @NotBlank(message = "Username or email cannot be blank")
    private String usernameOrEmail;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
