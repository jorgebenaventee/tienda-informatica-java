package dev.clownsinformatics.tiendajava.rest.clients.models;



import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;


@Data
@Builder
@Table(name = "CLIENTES")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El usuario no puede estar vacio.")
    private String username;

    @NotBlank(message = "El nombre no puede estar vacio.")
    private String name;

    @PositiveOrZero(message = "El balance no puede ser negativo.")
    private Double balance;

    @NotBlank(message = "El email no puede estar vacio.")
    private String email;

    @NotBlank(message = "La direccion no puede estar vacia.")
    private String address;

    @NotBlank(message = "El telefono no puede estar vacio.")
    private String phone;

    @NotBlank(message = "La fecha de nacimiento no puede estar vacia.")
    private LocalDate birthdate;

    private String image;




}
