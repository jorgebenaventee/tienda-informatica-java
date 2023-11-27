package dev.clownsinformatics.tiendajava.rest.users.dto;

import dev.clownsinformatics.tiendajava.rest.users.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Name cannot be empty")
    @Schema(description = "User´s name", example = "David")
    private String name;

    @NotBlank(message = "Last name cannot be empty")
    @Schema(description = "User´s last name", example = "Jaraba")
    private String lastName;

    @NotBlank(message = "Username cannot be empty")
    @Schema(description = "User´s username", example = "davidjaraba")
    private String username;

    @Email(regexp = ".*@.*\\..*", message = "Email must be valid")
    @NotBlank(message = "Email cannot be empty")
    @Schema(description = "User´s email", example = "david@gmail.com")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Length(min = 5, message = "Password must be at least 5 characters long")
    @Schema(description = "User´s password", example = "12345")
    private String password;

    @Builder.Default
    @Schema(description = "User´s roles", example = "[USER]")
    private Set<Role> roles = Set.of(Role.USER);

    @Builder.Default
    @Schema(description = "User´s status", example = "false")
    private Boolean isDeleted = false;
}
