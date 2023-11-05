package dev.clownsinformatics.tiendajava.rest.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFound extends ProductException {
    public ProductNotFound(String id) {
        super("No se encontró el producto con id: " + id);
    }
}
