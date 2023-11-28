package dev.clownsinformatics.tiendajava.rest.orders.mappers;

import dev.clownsinformatics.tiendajava.rest.clients.dto.ClientResponse;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderCreateDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderResponseDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderUpdateDto;
import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de mapear entre diferentes representaciones de la entidad Order.
 */
@Component
public class OrderMapper {
    /**
     * Convierte un OrderCreateDto y un ClientResponse en una entidad Order.
     *
     * @param orderCreateDto Datos para la creación del pedido.
     * @param client         Cliente asociado al pedido.
     * @return Entidad Order creada.
     */
    public Order toOrder(OrderCreateDto orderCreateDto, ClientResponse client) {
        return Order.builder()
                .idUser(orderCreateDto.idUser())
                .client(client)
                .orderLines(orderCreateDto.orderLines())
                .build();
    }

    /**
     * Convierte un OrderUpdateDto, un Order existente y un ClientResponse en una entidad Order actualizada.
     *
     * @param orderUpdateDto Datos actualizados para el pedido.
     * @param order          Pedido existente que se va a actualizar.
     * @param client         Cliente asociado al pedido.
     * @return Entidad Order actualizada.
     */
    public Order toOrder(OrderUpdateDto orderUpdateDto, Order order, ClientResponse client) {
        return Order.builder()
                .id(order.getId())
                .idUser(orderUpdateDto.idUser() != null ? orderUpdateDto.idUser() : order.getIdUser())
                .client(client)
                .orderLines(orderUpdateDto.orderLines() != null ? orderUpdateDto.orderLines() : order.getOrderLines())
                .build();
    }

    /**
     * Convierte una entidad Order en un OrderResponseDto para enviar como respuesta.
     *
     * @param order Entidad Order.
     * @return DTO de respuesta para la entidad Order.
     */
    public OrderResponseDto toOrderResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId().toHexString(),
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
