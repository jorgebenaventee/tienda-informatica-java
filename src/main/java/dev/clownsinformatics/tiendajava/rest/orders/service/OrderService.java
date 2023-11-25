package dev.clownsinformatics.tiendajava.rest.orders.service;

import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<Order> findAll(Pageable pageable);

    Order findById(ObjectId id);

    Page<Order> findByUserId(Long idUser, Pageable pageable);

    Order save(Order order);

    void delete(ObjectId objectId);

    Order update(ObjectId objectId, Order order);
}
