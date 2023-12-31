package dev.clownsinformatics.tiendajava.rest.storage.exceptions;

import java.io.Serial;

/**
 * Excepción de almacenamiento.
 */
public abstract class StorageException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    protected StorageException(String message) {
        super(message);
    }
}
