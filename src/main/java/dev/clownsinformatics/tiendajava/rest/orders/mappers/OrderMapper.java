package dev.clownsinformatics.tiendajava.rest.orders.mappers;

import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderCreateDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderResponseDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderUpdateDto;
import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order toOrder(OrderCreateDto orderCreateDto, ClientResponse client) {
        return Order.builder()
                .idUser(orderCreateDto.idUser())
                .client(client)
                .orderLines(orderCreateDto.orderLines())
                .build();
    }

    public Order toOrder(OrderUpdateDto orderUpdateDto, Order order, ClientResponse client) {
        return Order.builder()
                .id(order.getId())
                .idUser(orderUpdateDto.idUser() != null ? orderUpdateDto.idUser() : order.getIdUser())
                .client(client)
                .orderLines(orderUpdateDto.orderLines() != null ? orderUpdateDto.orderLines() : order.getOrderLines())
                .build();
    }

    public Order toOrder(OrderUpdateDto orderUpdateDto, Order order) {
        return Order.builder()
                .id(order.getId())
                .idUser(orderUpdateDto.idUser() != null ? orderUpdateDto.idUser() : order.getIdUser())
                .client(order.getClient())
                .orderLines(orderUpdateDto.orderLines() != null ? orderUpdateDto.orderLines() : order.getOrderLines())
                .build();
    }

    public OrderResponseDto toOrderResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getIdUser(),
                order.getClient(),
                order.getOrderLines(),
                order.getTotalItems(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getIsDeleted()
        );
    }
}
