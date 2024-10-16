package com.banker.duckrace.configuration;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class DuckRaceWebSocketHandler extends TextWebSocketHandler {

    // To store all the active WebSocket sessions
    private ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Add the new session to the map of sessions
        sessions.put(session.getId(), session);
        System.out.println("New connection established. Session ID: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages from clients
        String clientMessage = message.getPayload();
        System.out.println("Received message: " + clientMessage + " from session ID: " + session.getId());

        // Broadcast the message to all connected clients
        for (WebSocketSession ws : sessions.values()) {
            if (ws.isOpen()) {
                ws.sendMessage(new TextMessage("Broadcast: " + clientMessage));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Remove the session when the connection is closed
        sessions.remove(session.getId());
        System.out.println("Connection closed. Session ID: " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error occurred in session ID: " + session.getId() + ", Error: " + exception.getMessage());
        session.close();
        sessions.remove(session.getId());
    }
}
