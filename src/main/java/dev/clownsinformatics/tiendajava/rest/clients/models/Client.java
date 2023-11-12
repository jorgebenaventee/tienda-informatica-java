package dev.clownsinformatics.tiendajava.rest.clients.models;


import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;


@Data
@Builder
@Entity
@Table(name = "CLIENTS")
@AllArgsConstructor
@NoArgsConstructor(force = true)
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

    @NotBlank(message = "El telefono no puede estar vacio.") @NumberFormat(
            style = NumberFormat.Style.NUMBER,
            pattern = "########"
    )
    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private String image;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted;




}
