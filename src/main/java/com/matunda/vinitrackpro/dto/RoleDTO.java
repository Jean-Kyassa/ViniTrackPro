package com.matunda.vinitrackpro.dto;

import com.matunda.vinitrackpro.model.Role.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RoleDTO {

    private Long id;
    private RoleType name;
}
