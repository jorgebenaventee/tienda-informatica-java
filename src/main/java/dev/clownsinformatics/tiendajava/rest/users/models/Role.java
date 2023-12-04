package dev.clownsinformatics.tiendajava.rest.users.models;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Roles de usuario
 */
public enum Role {
    @Schema(description = "Rol de usuario normal")
    USER,
    @Schema(description = "Rol de administrador")
    ROLE_USER, ADMIN
}