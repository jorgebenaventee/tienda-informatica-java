package dev.clownsinformatics.tiendajava.rest.employees.exceptions;

public abstract class EmployeeException extends RuntimeException {
    public EmployeeException(String message) {
        super(message);
    }
}
