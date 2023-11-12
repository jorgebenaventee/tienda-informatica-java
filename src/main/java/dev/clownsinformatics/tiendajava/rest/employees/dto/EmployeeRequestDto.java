package dev.clownsinformatics.tiendajava.rest.employees.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


public record EmployeeRequestDto(
        @NotBlank
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        String name,
        @DecimalMin(value = "0.0", message = "The salary must be greater than or equal to 0")
        Double salary,
        @NotBlank
        @Length(min = 3, max = 50, message = "The position must be between 3 and 50 characters")
        String position
) {
}
