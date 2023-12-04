package dev.clownsinformatics.tiendajava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase que representa una excepción de orden sin items.
 * @version 1.0
 * @since 1.0
 * @see OrderException
 * @see ResponseStatus
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderNotItems extends OrderException {
    public OrderNotItems(String id) {
        super("Order " + id + " has no items");
    }
}
