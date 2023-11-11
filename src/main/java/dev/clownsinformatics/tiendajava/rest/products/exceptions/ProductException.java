package dev.clownsinformatics.tiendajava.rest.products.exceptions;

public abstract class ProductException extends RuntimeException {
    protected ProductException(String message) {
        super(message);
    }
}
