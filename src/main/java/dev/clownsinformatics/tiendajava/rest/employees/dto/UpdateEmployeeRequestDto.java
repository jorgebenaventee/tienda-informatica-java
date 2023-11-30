package dev.clownsinformatics.tiendajava.rest.employees.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import org.hibernate.validator.constraints.Length;

public record UpdateEmployeeRequestDto(
        @Length(min = 3, max = 50, message = "The name must be between 3 and 50 characters")
        @Schema(description = "The name of the employee", example = "Jorge")
        String name,
        @DecimalMin(value = "0.0", message = "The salary must be greater than or equal to 0")
        @Schema(description = "The salary of the employee", example = "1000.0")
        Double salary,
        @Length(min = 3, max = 50, message = "The position must be between 3 and 50 characters")
        @Schema(description = "The position of the employee", example = "Desarrollador web")
        String position

) {
}
