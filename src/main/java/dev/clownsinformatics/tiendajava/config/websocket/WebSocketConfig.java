package dev.clownsinformatics.tiendajava.config.websocket;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Clase de configuración que habilita la funcionalidad de WebSocket en la aplicación.
 * <p>
 * Esta clase utiliza las anotaciones {@code @Configuration} y {@code @EnableWebSocket} para
 * configurar y habilitar el soporte WebSocket.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    /**
     * Registra los manejadores WebSocket y los mapea a las rutas específicas.
     *
     * @param registry El registro de manejadores WebSocket.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketProductHandler(), "/ws/product");
        registry.addHandler(webSocketCategoryHandler(), "/ws/category");
        registry.addHandler(webSocketSupplierHandler(), "/ws/suppliers");
        registry.addHandler(webSocketEmployeeHandler(), "/ws/employee");
        registry.addHandler(webSocketClientHandler(), "/ws/clients");
    }

    /**
     * Crea y devuelve un manejador WebSocket para la entidad "Supplier".
     *
     * @return Un objeto {@code WebSocketHandler} para la entidad "Supplier".
     */
    @Bean
    public WebSocketHandler webSocketSupplierHandler() {
        return new WebSocketHandler("Suppliers");
    }

    /**
     * Crea y devuelve un manejador WebSocket para la entidad "Product".
     *
     * @return Un objeto {@code WebSocketHandler} para la entidad "Product".
     */
    @Bean
    public WebSocketHandler webSocketProductHandler() {
        return new WebSocketHandler("Product");
    }

    /**
     * Crea y devuelve un manejador WebSocket para la entidad "Category".
     *
     * @return Un objeto {@code WebSocketHandler} para la entidad "Category".
     */
    @Bean
    public WebSocketHandler webSocketCategoryHandler() {
        return new WebSocketHandler("Category");
    }

    /**
     * Crea y devuelve un manejador WebSocket para la entidad "Employee".
     *
     * @return Un objeto {@code WebSocketHandler} para la entidad "Employee".
     */
    @Bean
    public WebSocketHandler webSocketEmployeeHandler() {
        return new WebSocketHandler("Employee");
    }

    /**
     * Crea y devuelve un manejador WebSocket para la entidad "Client".
     *
     * @return Un objeto {@code WebSocketHandler} para la entidad "Client".
     */
    @Bean
    public WebSocketHandler webSocketClientHandler() {
        return new WebSocketHandler("Client");
    }

}