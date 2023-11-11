package dev.clownsinformatics.tiendajava.rest.employees.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity

@EntityListeners(AuditingEntityListener.class)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
    private String name;
    @DecimalMin(value = "0.0", message = "The salary must be greater than or equal to 0")
    private Double salary;
    @NotBlank
    @Length(min = 3, max = 50, message = "The position must be between 3 and 50 characters")
    private String position;
}
