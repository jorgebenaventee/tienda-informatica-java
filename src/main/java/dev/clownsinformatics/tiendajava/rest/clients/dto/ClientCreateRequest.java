package dev.clownsinformatics.tiendajava.rest.clients.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import jakarta.validation.constraints.*;


public record ClientCreateRequest(
        @NotBlank(message = "User can not be empty.") String username,
        @NotBlank(message = "Name can not be empty.") String name,
        @PositiveOrZero(message = "Balance must be positive or zero.") Double balance,
        @NotBlank(message = "Email can not be empty.") @Email(message = "Email must have a valid format.") String email,
        @NotBlank(message = "Address can not be empty.") String address,
        @NotBlank(message = "Phone number must have 9 digits.") @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must have 9 digits.") String phone,
        @NotBlank(message = "Birthday can not be empty.") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birthday must have this format yyyy-MM-dd.") String birthdate,
        String image,
        Boolean isDeleted) {


}
