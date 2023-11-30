package dev.clownsinformatics.tiendajava.rest.categories.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "CATEGORIES")
@EntityListeners(AuditingEntityListener.class)

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    @Schema(description = "The unique identifier of the category", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
    @Schema(description = "The name of the category", example = "PORTATILES")
    private String name;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    @Builder.Default
    @Schema(description = "The date and time when the category was created", example = "2021-10-10T10:10:10")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable = false)
    @Builder.Default
    @Schema(description = "The date and time when the category was last updated", example = "2021-10-10T10:10:10")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private boolean isDeleted = false;
}
