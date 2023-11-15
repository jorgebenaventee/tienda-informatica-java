package dev.clownsinformatics.tiendajava.rest.clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ClientUpdateRequest(
        String username,
        String name,
        @PositiveOrZero(message = "Balance must be positive or zero.") Double balance,
        @Email(message = "Email must have a valid format.") String email,
        String address,
        String phone,
        @NotBlank(message = "Birthday can not be empty.") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birthday must have this format yyyy-MM-dd.") String birthdate,
        String image, Boolean isDeleted) {


}
