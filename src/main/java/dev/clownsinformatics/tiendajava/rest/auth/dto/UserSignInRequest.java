package dev.clownsinformatics.tiendajava.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInRequest {
    @NotBlank(message = "Username cannot be empty")
    @Schema(description = "Username", example = "admin")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Length(min = 5, message = "Password must be at least 5 characters long")
    @Schema(description = "Password must be at least 5 characters long", example = "admin1")
    private String password;
}
