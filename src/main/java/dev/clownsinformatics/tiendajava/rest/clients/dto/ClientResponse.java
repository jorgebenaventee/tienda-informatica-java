package dev.clownsinformatics.tiendajava.rest.clients.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;



public record ClientResponse(
        Long id,
        @NotBlank(message = "El usuario no puede estar vacio.") String username,
        @NotBlank(message = "El nombre no puede estar vacio.") String name,
        @PositiveOrZero(message = "El balance no puede ser negativo.") Double balance,
        @NotBlank(message = "El email no puede estar vacio.") String email,
        @NotBlank(message = "La direccion no puede estar vacia.") String address,
        @NotBlank(message = "El telefono no puede estar vacio.") String phone,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
        String image, Boolean isDeleted) {




}
