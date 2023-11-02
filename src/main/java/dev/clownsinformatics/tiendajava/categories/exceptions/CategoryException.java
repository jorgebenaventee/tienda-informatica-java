package dev.clownsinformatics.tiendajava.categories.exceptions;

public abstract class CategoryException extends RuntimeException{
    protected CategoryException(String message){
        super(message);
    }
}
