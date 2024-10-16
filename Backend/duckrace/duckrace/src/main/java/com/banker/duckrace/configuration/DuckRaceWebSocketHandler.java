package com.banker.duckrace.configuration;

import com.banker.duckrace.model.Player;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class DuckRaceWebSocketHandler extends TextWebSocketHandler {

    // Map to store session ID and associated Player information
    private ConcurrentHashMap<String, Player> PlayerSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // When a new Player connects, assign them a default name and points (you can adjust this logic)
        Player newPlayer = new Player("Player_" + session.getId(), 0); // Default name and 0 points
        PlayerSessions.put(session.getId(), newPlayer);

        System.out.println("New connection: " + session.getId() + " with name: " + newPlayer.getName());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages (e.g., setting name or updating points)
        String clientMessage = message.getPayload();

        // Example: Client sends "SET_NAME:John"
        if (clientMessage.startsWith("SET_NAME:")) {
            String newName = clientMessage.split(":")[1];
            Player Player = PlayerSessions.get(session.getId());
            if (Player != null) {
                Player.setName(newName);
                session.sendMessage(new TextMessage("Name set to: " + newName));
            }
        }

        // Example: Client sends "ADD_POINTS:10"
        if (clientMessage.startsWith("ADD_POINTS:")) {
            int points = Integer.parseInt(clientMessage.split(":")[1]);
            Player Player = PlayerSessions.get(session.getId());
            if (Player != null) {
                Player.addPoints(points);
                session.sendMessage(new TextMessage("Points updated. Current points: " + Player.getPoints()));
            }
        }

        System.out.println("Message received from " + session.getId() + ": " + clientMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Remove the session when the connection is closed
        PlayerSessions.remove(session.getId());
        System.out.println("Connection closed: " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Error in session " + session.getId() + ": " + exception.getMessage());
        session.close();
        PlayerSessions.remove(session.getId());
    }
}
