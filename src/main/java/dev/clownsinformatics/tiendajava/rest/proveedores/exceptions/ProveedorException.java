package dev.clownsinformatics.tiendajava.rest.proveedores.exceptions;


public abstract class ProveedorException extends RuntimeException{

    protected ProveedorException(String message) {
        super(message);
    }
}