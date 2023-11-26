package dev.clownsinformatics.tiendajava.rest.orders.dto;

import dev.clownsinformatics.tiendajava.rest.orders.models.OrderLine;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderUpdateDto(
        @Positive(message = "The idUser must be greater than 0")
        Long idUser,
        List<OrderLine> orderLines
) {
}
