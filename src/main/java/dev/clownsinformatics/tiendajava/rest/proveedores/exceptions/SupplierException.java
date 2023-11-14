package dev.clownsinformatics.tiendajava.rest.proveedores.exceptions;


public abstract class SupplierException extends RuntimeException {

    protected SupplierException(String message) {
        super(message);
    }
}