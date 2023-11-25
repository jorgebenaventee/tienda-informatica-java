package dev.clownsinformatics.tiendajava.rest.orders.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderBadPrice extends OrderException {
    public OrderBadPrice(String id) {
        super("Order " + id + " has a bad price");
    }
}
