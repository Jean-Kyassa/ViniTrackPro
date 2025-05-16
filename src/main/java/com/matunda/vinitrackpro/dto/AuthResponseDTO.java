package com.matunda.vinitrackpro.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String accessToken;
    private UserDTO user;
    private static final String tokenType = "Bearer";
}