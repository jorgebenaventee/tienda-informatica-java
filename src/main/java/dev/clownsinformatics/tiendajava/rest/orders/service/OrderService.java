package dev.clownsinformatics.tiendajava.rest.orders.service;

import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderCreateDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderResponseDto;
import dev.clownsinformatics.tiendajava.rest.orders.dto.OrderUpdateDto;
import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderResponseDto> findAll(Pageable pageable);

    OrderResponseDto findById(ObjectId id);

    Page<OrderResponseDto> findByUserId(Long idUser, Pageable pageable);

    OrderResponseDto save(OrderCreateDto order);

    void delete(ObjectId objectId);

    OrderResponseDto update(ObjectId objectId, OrderUpdateDto order);
}
