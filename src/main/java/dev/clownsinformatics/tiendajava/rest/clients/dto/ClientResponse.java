package dev.clownsinformatics.tiendajava.rest.clients.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
public record ClientResponse(
        Long id,
        @NotBlank(message = "User can not be empty.")
        String username,
        @NotBlank(message = "Name can not be empty.")
        String name,
        @PositiveOrZero(message = "Balance must be positive or zero.")
        Double balance,
        @NotBlank(message = "Email can not be empty.")
        String email,
        @NotBlank(message = "Address can not be empty.")
        String address,
        @NotBlank(message = "Phone number must have 9 digits.")
        String phone,
        LocalDate birthdate,
        String image,
        Boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
