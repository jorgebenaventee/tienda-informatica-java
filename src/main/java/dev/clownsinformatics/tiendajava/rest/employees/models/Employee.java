package dev.clownsinformatics.tiendajava.rest.employees.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity

@EntityListeners(AuditingEntityListener.class)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The employee's id", example = "1")
    private Integer id;
    @NotBlank
    @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
    @Schema(description = "The employee's name", example = "Jorge")
    private String name;
    @DecimalMin(value = "0.0", message = "The salary must be greater than or equal to 0")
    @Schema(description = "The employee's salary", example = "1000.0")
    private Double salary;
    @NotBlank
    @Length(min = 3, max = 50, message = "The position must be between 3 and 50 characters")
    @Schema(description = "The employee's position", example = "Desarrollador web")
    private String position;


    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    @Builder.Default
    @Schema(description = "The employee's creation date", example = "2021-10-10T00:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable = false)
    @Builder.Default
    @Schema(description = "The employee's last update date", example = "2021-10-10T00:00:00")
    private LocalDateTime updatedAt = LocalDateTime.now();

}
