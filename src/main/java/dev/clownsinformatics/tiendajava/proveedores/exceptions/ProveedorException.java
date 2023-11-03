package dev.clownsinformatics.tiendajava.proveedores.exceptions;


public abstract class ProveedorException extends RuntimeException{

    protected ProveedorException(String message) {
        super(message);
    }
}