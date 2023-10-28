package dev.clownsinformatics.tiendajava.products.exceptions;

public abstract class ProductException extends RuntimeException{
    protected ProductException(String message) {
        super(message);
    }
}
