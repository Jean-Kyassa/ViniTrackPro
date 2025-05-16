package com.matunda.vinitrackpro.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    public enum RoleType {
        ROLE_COORDINATOR,       // Admin
        ROLE_LOGISTICS_MANAGER,
        ROLE_LOGISTICS_EXPERT,
        ROLE_INDUSTRY_MANAGER,
        ROLE_ACCOUNTANT,
        ROLE_DISTRIBUTION_MANAGER,
        ROLE_QUALITY_CONTROL,
        ROLE_USER,
        ROLE_SALES_STAFF,
        ROLE_CLIENT
    }
}
