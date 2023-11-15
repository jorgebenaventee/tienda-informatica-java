package dev.clownsinformatics.tiendajava.rest.clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;


public record ClientUpdateRequest(
        @Length(min = 1, message = "User can not be empty.") String username,
        @Length(min = 1, message = "Name can not be empty.") String name,
        @PositiveOrZero(message = "Balance must be positive or zero.") Double balance,
        @Length(min = 1, message = "Email can not be empty.") @Email(message = "Email must have a valid format.") String email,
        @Length(min = 1, message = "Address can not be empty.") String address,
        @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must have 9 digits.") String phone,
        @Length(min = 1, message = "Date can not be empty.") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birthday must have this format yyyy-MM-dd.") String birthdate,
        String image, Boolean isDeleted) {


}
