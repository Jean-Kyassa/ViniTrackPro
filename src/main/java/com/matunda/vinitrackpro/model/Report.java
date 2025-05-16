package com.matunda.vinitrackpro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Column(nullable = false)
    private LocalDateTime reportDate;

    @Column(nullable = false)
    private LocalDateTime periodStart;

    @Column(nullable = false)
    private LocalDateTime periodEnd;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by", nullable = false)
    private User generatedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum ReportType {
        INVENTORY,
        SALES,
        PRODUCTION,
        LOGISTICS,
        QUALITY_CONTROL,
        FINANCIAL,
        CUSTOMER
    }
}
