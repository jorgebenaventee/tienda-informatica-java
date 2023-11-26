package dev.clownsinformatics.tiendajava.rest.orders.dto;


import dev.clownsinformatics.tiendajava.rest.orders.models.OrderLine;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderCreateDto(
        @NotNull(message = "The idUser is required")
        @Positive(message = "The idUser must be greater than 0")
        Long idUser,
        @NotNull(message = "The order should have at least one order line")
        List<OrderLine> orderLines
) {
}
