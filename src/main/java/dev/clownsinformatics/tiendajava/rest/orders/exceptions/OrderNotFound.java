package dev.clownsinformatics.tiendajava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase que representa una excepción de orden no encontrada.
 * @version 1.0
 * @since 1.0
 * @see OrderException
 * @see ResponseStatus
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFound extends OrderException {
    public OrderNotFound(String message) {
        super(message);
    }
}
