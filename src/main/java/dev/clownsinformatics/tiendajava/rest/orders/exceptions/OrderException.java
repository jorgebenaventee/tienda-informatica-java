package dev.clownsinformatics.tiendajava.rest.orders.exceptions;

/**
 * Clase abstracta que representa una excepción de orden.
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public abstract class OrderException extends RuntimeException {
    protected OrderException(String message) {
        super(message);
    }
}
