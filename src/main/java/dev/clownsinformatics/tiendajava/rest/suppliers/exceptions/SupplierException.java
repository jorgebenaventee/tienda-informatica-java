package dev.clownsinformatics.tiendajava.rest.suppliers.exceptions;


public abstract class SupplierException extends RuntimeException {

    protected SupplierException(String message) {
        super(message);
    }
}