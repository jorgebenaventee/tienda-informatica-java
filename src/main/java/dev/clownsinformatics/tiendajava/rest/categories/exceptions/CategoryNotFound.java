package dev.clownsinformatics.tiendajava.rest.categories.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion para cuando una categoria no existe
 * @version 1.0.0
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFound extends CategoryException {
    public CategoryNotFound(String message) {
        super(message);
    }
}
