package dev.clownsinformatics.tiendajava.rest.clients.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;



public record ClientResponse(
        Long id,
        @NotBlank(message = "El usuario no puede estar vacio.") String username,
        @NotBlank(message = "El nombre no puede estar vacio.") String name,
        @PositiveOrZero(message = "El balance no puede ser negativo.") Double balance,
        @NotBlank(message = "El email no puede estar vacio.") String email,
        @NotBlank(message = "La direccion no puede estar vacia.") String address,
        @NotBlank(message = "El telefono no puede estar vacio.") String phone,
        @NotBlank(message = "La fecha de nacimiento no puede estar vacia.") LocalDate birthdate,
        String image) {




}
