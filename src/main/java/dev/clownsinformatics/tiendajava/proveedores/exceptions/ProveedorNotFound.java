package dev.clownsinformatics.tiendajava.proveedores.exceptions;

import java.util.UUID;

public class ProveedorNotFound extends ProveedorException {

    public ProveedorNotFound(Long idProveedor) {
        super("No hay ningun proveedor con id " + idProveedor);
    }

    public ProveedorNotFound(UUID idEmpresa) {
        super("No hay ningun proveedor con id de empresa " + idEmpresa);

    }

}