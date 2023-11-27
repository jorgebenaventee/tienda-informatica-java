package dev.clownsinformatics.tiendajava.rest.clients.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;


public record ClientCreateRequest(
        @NotBlank(message = "User can not be empty.")
        @Schema(description = "Username of the client.", example = "username")
        String username,
        @NotBlank(message = "Name can not be empty.")
        @Schema(description = "Name of the client.", example = "Madre David")
        String name,
        @PositiveOrZero(message = "Balance must be positive or zero.")
        @Schema(description = "Balance of the client.", example = "10.0")
        Double balance,
        @NotBlank(message = "Email can not be empty.")
        @Email(message = "Email must have a valid format.")
        @Schema(description = "Email of the client.", example = "username@gmail.com")
        String email,
        @NotBlank(message = "Address can not be empty.")
        @Schema(description = "Address of the client.", example = "Calle 1")
        String address,
        @NotBlank(message = "Phone number must have 9 digits.")
        @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must have 9 digits.")
        @Schema(description = "Phone number of the client.", example = "123456789")
        String phone,
        @NotBlank(message = "Birthday can not be empty.")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birthday must have this format yyyy-MM-dd.")
        @Schema(description = "Birthday of the client.", example = "1999-01-01")
        String birthdate,
        @Schema(description = "Image of the client.", example = "image.jpg")
        String image,
        @Schema(description = "Is the client deleted.", example = "false")
        Boolean isDeleted) {


}
