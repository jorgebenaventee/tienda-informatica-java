package dev.clownsinformatics.tiendajava.rest.clients.models;


import jakarta.validation.constraints.Pattern;
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
    private Long id;

    @NotBlank(message = "User can not be empty.")
    private String username;

    @NotBlank(message = "Name can not be empty.")
    private String name;

    @PositiveOrZero(message = "Balance must be positive or zero.")
    private Double balance;

    @NotBlank(message = "El email no puede estar vacio.")
    private String email;

    @NotBlank(message = "Address can not be empty.")
    private String address;

    @NotBlank(message = "Phone number must have 9 digits.") @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must have 9 digits.")
    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private String image;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted;


    @Column(updatable = false, nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(updatable = true, nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();


}
