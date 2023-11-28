package dev.clownsinformatics.tiendajava.rest.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.orders.models.OrderLine;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        @JsonIgnoreProperties(ignoreUnknown = true)
        String id,
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
