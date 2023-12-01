package dev.clownsinformatics.tiendajava.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Manejador de WebSocket para la gestión de conexiones y mensajes en tiempo real.
 * Extiende de {@link TextWebSocketHandler} e implementa las interfaces {@link SubProtocolCapable} y {@link WebSocketSender}.
 *
 * Este manejador permite la comunicación bidireccional entre el servidor y los clientes a través del protocolo WebSocket.
 * Se encarga de gestionar la apertura, cierre y envío de mensajes a las sesiones WebSocket.
 * Además, implementa un método programado para el envío periódico de mensajes a todas las sesiones activas.
 */
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable, WebSocketSender {
    /**
     * Identificador único asociado a la entidad gestionada por este manejador.
     */
    private final String entity;

    /**
     * Conjunto de sesiones WebSocket activas. Se utiliza {@link CopyOnWriteArraySet} para garantizar
     * la seguridad en entornos concurrentes.
     */
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    /**
     * Constructor que inicializa el manejador con la entidad específica.
     *
     * @param entity Identificador único de la entidad asociada al manejador.
     */
    public WebSocketHandler(String entity) {
        this.entity = entity;
    }

    /**
     * Maneja las acciones después de establecerse la conexión WebSocket con el servidor.
     * Registra la sesión y envía un mensaje de bienvenida al cliente.
     *
     * @param session Sesión WebSocket establecida.
     * @throws Exception Si ocurre un error durante la ejecución.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Conexión establecida con el servidor");
        log.info("Sesión: " + session);
        sessions.add(session);
        TextMessage message = new TextMessage("Actualizaciones WebSocket: " + entity + " - Tienda informática API de Spring Boot");
        log.info("Servidor envía: {}", message);
        session.sendMessage(message);
    }

    /**
     * Maneja las acciones después de cerrarse la conexión WebSocket con el servidor.
     * Elimina la sesión cerrada del conjunto de sesiones.
     *
     * @param session Sesión WebSocket cerrada.
     * @param status  Estado de cierre.
     * @throws Exception Si ocurre un error durante la ejecución.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Conexión cerrada con el servidor: " + status);
        sessions.remove(session);
    }

    /**
     * Envía un mensaje a todas las sesiones WebSocket activas.
     *
     * @param message Mensaje a enviar.
     * @throws IOException Si hay un problema al enviar el mensaje.
     */
    @Override
    public void sendMessage(String message) throws IOException {
        log.info("Enviar mensaje de cambios en la entidad: " + entity + " : " + message);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                log.info("Servidor WS envía: " + message);
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    /**
     * Envía mensajes periódicos a todas las sesiones WebSocket activas.
     * Este método está programado para ejecutarse a intervalos fijos.
     *
     * @throws IOException Si hay un problema al enviar el mensaje.
     */
    @Scheduled(fixedRate = 1000)
    @Override
    public void sendPeriodicMessages() throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                String broadcast = "mensaje periódico del servidor " + LocalTime.now();
                log.info("El servidor envía: " + broadcast);
                session.sendMessage(new TextMessage(broadcast));
            }
        }
    }


    /**
     * Maneja los mensajes de texto recibidos en una sesión WebSocket.
     * Registra y muestra el mensaje recibido, luego reenvía el mismo mensaje a la sesión.
     *
     * @param session Sesión WebSocket que recibió el mensaje.
     * @param message Mensaje de texto recibido.
     * @throws Exception Si ocurre un error durante la ejecución.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Mensaje recibido: " + message);
        session.sendMessage(message);
    }

    /**
     * Maneja los errores de transporte en una sesión WebSocket.
     * Registra el mensaje de error asociado y realiza las acciones necesarias.
     *
     * @param session   Sesión WebSocket con error de transporte.
     * @param exception Excepción de transporte ocurrida.
     * @throws Exception Si ocurre un error durante la ejecución.
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Error de transporte con el servidor: " + exception.getMessage());
    }

    /**
     * Obtiene la lista de subprotocolos admitidos por este manejador WebSocket.
     * En este caso, devuelve un solo subprotocolo: "subprotocol.demo.websocket".
     *
     * @return Lista de subprotocolos admitidos.
     */
    @Override
    public List<String> getSubProtocols() {
        return List.of("subprotocol.demo.websocket");
    }
}