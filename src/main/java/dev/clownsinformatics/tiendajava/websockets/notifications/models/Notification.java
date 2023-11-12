package dev.clownsinformatics.tiendajava.websockets.notifications.models;

public record Notification<T>(
        String entity,
        Tipo type,
        T data,
        String createdAt
) {

    public enum Tipo {READ, CREATE, UPDATE, DELETE}

}