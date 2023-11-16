package dev.clownsinformatics.tiendajava.rest.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductBadRequest extends ProductException {
    public ProductBadRequest(String message) {
        super(message);
    }
}
