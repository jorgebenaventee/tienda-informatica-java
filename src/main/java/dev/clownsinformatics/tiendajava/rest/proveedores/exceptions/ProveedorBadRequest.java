package dev.clownsinformatics.tiendajava.rest.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProveedorBadRequest extends ProveedorException {
    public ProveedorBadRequest(String message) {
        super(message);
    }
}
