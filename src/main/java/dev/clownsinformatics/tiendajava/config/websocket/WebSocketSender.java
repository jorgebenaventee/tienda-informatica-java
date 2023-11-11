package dev.clownsinformatics.tiendajava.config.websocket;


import java.io.IOException;

public interface WebSocketSender {

        void sendMessage(String message) throws IOException;

        void sendPeriodicMessages() throws IOException;
    }
