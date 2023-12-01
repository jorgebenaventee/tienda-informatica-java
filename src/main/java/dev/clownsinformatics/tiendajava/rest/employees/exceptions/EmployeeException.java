package dev.clownsinformatics.tiendajava.rest.employees.exceptions;

/**
 * Excepción base para las excepciones de empleados
 */
public abstract class EmployeeException extends RuntimeException {
    protected EmployeeException(String message) {
        super(message);
    }
}
