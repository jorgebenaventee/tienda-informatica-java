package dev.clownsinformatics.tiendajava.rest.clients.models;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@Entity
@Table(name = "CLIENTS")
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Client's id", example = "1")
    private Long id;

    @NotBlank(message = "User can not be empty.")
    @Schema(description = "Client's username", example = "user")
    private String username;

    @NotBlank(message = "Name can not be empty.")
    @Schema(description = "Client's name", example = "Mama David")
    private String name;

    @PositiveOrZero(message = "Balance must be positive or zero.")
    @Schema(description = "Client's balance", example = "100.0")
    private Double balance;

    @NotBlank(message = "El email no puede estar vacio.")
    @Schema(description = "Client's email", example = "user@gmail.com")
    private String email;

    @NotBlank(message = "Address can not be empty.")
    @Schema(description = "Client's address", example = "Calle 1")
    private String address;

    @NotBlank(message = "Phone number must have 9 digits.")
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must have 9 digits.")
    @Schema(description = "Client's phone", example = "123456789")
    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Client's birthdate", example = "1999-01-01")
    private LocalDate birthdate;

    @Schema(description = "Client's image", example = "img.jpg")
    private String image;

    @Column(columnDefinition = "boolean default false")
    @Schema(description = "Client's status", example = "false")
    private Boolean isDeleted;


    @Column(updatable = false, nullable = false)
    @Builder.Default
    @Schema(description = "Client's creation date", example = "2021-01-01")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(updatable = true, nullable = false)
    @Builder.Default
    @Schema(description = "Client's update date", example = "2021-01-01")
    private LocalDateTime updatedAt = LocalDateTime.now();


}
