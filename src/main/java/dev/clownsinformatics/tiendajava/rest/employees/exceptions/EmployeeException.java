package dev.clownsinformatics.tiendajava.rest.employees.exceptions;

public abstract class EmployeeException extends RuntimeException {
    protected EmployeeException(String message) {
        super(message);
    }
}
