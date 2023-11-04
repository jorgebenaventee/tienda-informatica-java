package dev.clownsinformatics.tiendajava.rest.categories.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryConflict extends CategoryException{
    public CategoryConflict(String message){
        super(message);
    }
}
