package dev.clownsinformatics.tiendajava.rest.orders.dto;


import dev.clownsinformatics.tiendajava.rest.orders.models.OrderLine;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderCreateDto(
        @NotNull(message = "The idUser is required")
        @Positive(message = "The idUser must be greater than 0")
        @Schema(description = "The id of the user that made the order", example = "1")
        Long idUser,
        @NotNull(message = "The order should have at least one order line")
        @Schema(description = "The order lines of the order", example = "[{\"idProduct\": 1, \"quantity\": 2}]")
        List<OrderLine> orderLines
) {
}
