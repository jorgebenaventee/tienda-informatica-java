package dev.clownsinformatics.tiendajava.rest.employees.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para cuando no se encuentra un empleado
 * Status code: 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends EmployeeException {
    public EmployeeNotFoundException(Integer id) {
        super("Employee with id " + id + " not found");
    }
}
