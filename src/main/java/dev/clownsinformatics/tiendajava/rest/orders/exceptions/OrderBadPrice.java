package dev.clownsinformatics.tiendajava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando se intenta crear una orden con un precio inválido.
 * @version 1.0.0
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderBadPrice extends OrderException {
    public OrderBadPrice(String id) {
        super("Order " + id + " has a bad price");
    }
}
