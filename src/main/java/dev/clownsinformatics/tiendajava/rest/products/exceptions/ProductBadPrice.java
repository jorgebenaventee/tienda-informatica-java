package dev.clownsinformatics.tiendajava.rest.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductBadPrice extends ProductException{
    public ProductBadPrice(String message) {
        super(message);
    }
}
