package dev.clownsinformatics.tiendajava.config.websocket;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketProveedorHandler(), "/ws/proveedor");
    }

    //ws://localhost:8080/ws/proveedor

    @Bean
    public WebSocketHandler webSocketProveedorHandler() {
        return new WebSocketHandler("Proveedor");
    }
}