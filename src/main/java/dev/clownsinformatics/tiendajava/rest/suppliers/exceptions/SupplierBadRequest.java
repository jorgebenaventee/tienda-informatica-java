package dev.clownsinformatics.tiendajava.rest.suppliers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SupplierBadRequest extends SupplierException {
    public SupplierBadRequest(String message) {
        super(message);
    }
}
