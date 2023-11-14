package dev.clownsinformatics.tiendajava.rest.proveedores.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SupplierNotFound extends SupplierException {

    public SupplierNotFound(UUID idProveedor) {
        super("There is not any proveedor with ID: " + idProveedor);

    }

}