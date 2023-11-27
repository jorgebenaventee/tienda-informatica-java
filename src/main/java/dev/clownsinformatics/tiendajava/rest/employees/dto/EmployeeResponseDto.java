package dev.clownsinformatics.tiendajava.rest.employees.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record EmployeeResponseDto(
        @Schema(description = "Employee's id", example = "1")
        Integer id,
        @Schema(description = "Employee's name", example = "Jorge")
        String name,
        @Schema(description = "Employee's salary", example = "1000.0")
        Double salary,
        @Schema(description = "Employee's position", example = "Desarrollador web")
        String position,
        @Schema(description = "Employee's createdAt", example = "2021-10-10T10:10:10")
        LocalDateTime createdAt,
        @Schema(description = "Employee's updatedAt", example = "2021-10-10T10:10:10")
        LocalDateTime updatedAt) {
}
