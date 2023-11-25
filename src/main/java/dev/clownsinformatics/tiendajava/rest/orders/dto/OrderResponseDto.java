package dev.clownsinformatics.tiendajava.rest.orders.dto;

import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.orders.models.OrderLine;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        ObjectId id,
        Long idUser,
        ClientResponse client,
        List<OrderLine> orderLines,
        Integer totalItems,
        Double total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isDeleted
) {
}
