package dev.clownsinformatics.tiendajava.rest.products.exceptions;

/**
 * Clase abstracta para las excepciones de productos
 */
public abstract class ProductException extends RuntimeException {
    protected ProductException(String message) {
        super(message);
    }
}
