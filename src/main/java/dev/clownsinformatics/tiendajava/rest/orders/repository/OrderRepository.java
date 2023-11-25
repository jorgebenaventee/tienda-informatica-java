package dev.clownsinformatics.tiendajava.rest.orders.repository;


import dev.clownsinformatics.tiendajava.rest.orders.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    Page<Order> findByIdUser(Long idUser, Pageable pageable);

    List<Order> findOrderIdsByIdUser(Long idUser);

    boolean existsByIdUser(Long idUser);
}
