package dev.clownsinformatics.tiendajava.rest.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion para cuando no se encuentra un producto
 * Status 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFound extends ProductException {
    public ProductNotFound(String id) {
        super("Product with id " + id + " not found");
    }
}
