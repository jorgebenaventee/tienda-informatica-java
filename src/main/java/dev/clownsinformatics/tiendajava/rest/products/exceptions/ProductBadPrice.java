package dev.clownsinformatics.tiendajava.rest.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando el precio de un producto es inválido.
 * Status 400
 * @version 1.0.0
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductBadPrice extends ProductException {
    public ProductBadPrice(String message) {
        super(message);
    }
}
