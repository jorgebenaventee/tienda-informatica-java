package dev.clownsinformatics.tiendajava.rest.suppliers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SupplierNotFound extends SupplierException {

    public SupplierNotFound(UUID id) {
        super("There is not any supplier with ID: " + id);

    }

    public SupplierNotFound(String name) {
        super("There is not any supplier with name: " + name);

    }

}