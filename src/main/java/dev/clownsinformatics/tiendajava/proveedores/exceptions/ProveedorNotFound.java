package dev.clownsinformatics.tiendajava.proveedores.exceptions;

import java.util.UUID;

public class ProveedorNotFound extends ProveedorException {

    public ProveedorNotFound(UUID idProveedor) {
        super("No hay ningun proveedor con id de empresa " + idProveedor);

    }

}