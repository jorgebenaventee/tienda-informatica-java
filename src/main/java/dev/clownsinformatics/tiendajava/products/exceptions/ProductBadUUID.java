package dev.clownsinformatics.tiendajava.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductBadUUID extends ProductException {
    public ProductBadUUID(String uuid) {
        super("UUID " + uuid + " no es valido");
    }
}
