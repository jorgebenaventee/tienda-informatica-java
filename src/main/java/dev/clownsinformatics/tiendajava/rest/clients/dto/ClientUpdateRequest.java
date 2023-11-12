package dev.clownsinformatics.tiendajava.rest.clients.dto;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ClientUpdateRequest(
        String username,
        String name,
        @PositiveOrZero(message = "El balance no puede ser negativo.") Double balance,
        String email,
        String address,
        String phone,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
        String image, Boolean isDeleted) {




}
