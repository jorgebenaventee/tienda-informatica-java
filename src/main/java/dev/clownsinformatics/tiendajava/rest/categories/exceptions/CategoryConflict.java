package dev.clownsinformatics.tiendajava.rest.categories.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion para cuando una categoria ya existe
 * @version 1.0.0
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryConflict extends CategoryException {
    public CategoryConflict(String message) {
        super(message);
    }
}
