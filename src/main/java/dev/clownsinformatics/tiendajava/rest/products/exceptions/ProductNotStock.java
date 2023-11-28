package dev.clownsinformatics.tiendajava.rest.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion para cuando no hay suficiente stock de un producto
 * Status 400
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNotStock extends ProductException {
    public ProductNotStock(String message) {
        super(message);
    }
}
