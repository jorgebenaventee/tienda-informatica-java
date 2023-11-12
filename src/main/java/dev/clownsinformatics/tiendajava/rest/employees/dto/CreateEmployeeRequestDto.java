package dev.clownsinformatics.tiendajava.rest.employees.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


public record CreateEmployeeRequestDto(
        @NotBlank(message = "The name cannot be blank")
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        String name,
        @NotNull(message = "The salary cannot be null")
        @DecimalMin(value = "0.0", message = "The salary must be greater than or equal to 0")
        Double salary,
        @NotBlank(message = "The position cannot be blank")
        @Length(min = 3, max = 50, message = "The position must be between 3 and 50 characters")
        String position
) {
}
