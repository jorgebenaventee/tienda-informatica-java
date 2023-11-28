package dev.clownsinformatics.tiendajava.rest.categories.exceptions;

/**
 * Clase abstracta para excepciones de categorias
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class CategoryException extends RuntimeException {
    protected CategoryException(String message) {
        super(message);
    }
}
