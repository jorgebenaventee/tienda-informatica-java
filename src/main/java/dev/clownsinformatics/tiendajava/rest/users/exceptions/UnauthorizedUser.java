package dev.clownsinformatics.tiendajava.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * UnathorizedUser
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedUser extends UserException{
    public UnauthorizedUser(String message){
        super(message);
    }
}
