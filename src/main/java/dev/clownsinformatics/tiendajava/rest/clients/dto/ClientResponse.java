package dev.clownsinformatics.tiendajava.rest.clients.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
public record ClientResponse(
        Long id,
        @NotBlank(message = "User can not be empty.") String username,
        @NotBlank(message = "Name can not be empty.") String name,
        @PositiveOrZero(message = "Balance must be positive or zero.") Double balance,
        @NotBlank(message = "El email no puede estar vacio.") String email,
        @NotBlank(message = "Address can not be empty.") String address,
        @NotBlank(message = "Phone number must have 9 digits.") String phone,
        @Pattern(regexp = "^\\\\d{4}-\\\\d{2}-\\\\d{2}$", message = "Birthday must have this format yyyy-MM-dd.") LocalDate birthdate,
        String image, Boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt){




}
