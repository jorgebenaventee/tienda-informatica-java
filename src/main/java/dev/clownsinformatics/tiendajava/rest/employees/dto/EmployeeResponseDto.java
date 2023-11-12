package dev.clownsinformatics.tiendajava.rest.employees.dto;

import java.time.LocalDateTime;

public record EmployeeResponseDto(Integer id, String name, Double salary, String position, LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
}
