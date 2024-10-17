package com.banker.duckrace.configuration;

import com.banker.duckrace.constant.PlayerService;
import com.banker.duckrace.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class DuckRaceWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private PlayerService playerService;



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // When a new Player connects, assign them a default name and points
        Player newPlayer = new Player("Player_" + session.getId(), 0);
        playerService.getPlayerSessions().put(session.getId(), newPlayer);

        // Store the WebSocket session
        playerService.getWebSocketSessions().put(session.getId(), session);

        // Broadcast updated player list to all clients
        broadcastPlayerListToAll();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Remove the player and WebSocket session when they disconnect
        playerService.getPlayerSessions().remove(session.getId());
        playerService.getWebSocketSessions().remove(session.getId());

        // Broadcast updated player list to all clients
        broadcastPlayerListToAll();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();

        // Handle name change
        if (clientMessage.startsWith("SET_NAME:")) {
            String newName = clientMessage.split(":")[1];
            Player player = playerService.getPlayerSessions().get(session.getId());
            if (player != null) {
                player.setName(newName);
                // Broadcast updated player list to all clients
                broadcastPlayerListToAll();
            }
        }

        // Handle point update
        if (clientMessage.startsWith("ADD_POINTS:")) {
            int points = Integer.parseInt(clientMessage.split(":")[1]);
            Player player = playerService.getPlayerSessions().get(session.getId());
            if (player != null) {
                player.addPoints(points);
                // Broadcast updated player list to all clients
                broadcastPlayerListToAll();
            }
        }
    }


    // Broadcast the full player list to all connected clients
    private void broadcastPlayerListToAll() throws IOException {
        // Convert player list to JSON
        String playerListJson = new ObjectMapper().writeValueAsString(playerService.getPlayerSessions().values());

        // Send the updated player list to all connected clients
        for (WebSocketSession clientSession : playerService.getWebSocketSessions().values()) {
            if (clientSession.isOpen()) {
                clientSession.sendMessage(new TextMessage(playerListJson));
            }
        }
    }

    // Optional: Add methods to handle other types of messages as needed
    // error handling, etc.

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle transport errors (e.g., client disconnects)
        // Remove the player and WebSocket session when they disconnect
        playerService.getPlayerSessions().remove(session.getId());
        playerService.getWebSocketSessions().remove(session.getId());

        // Broadcast updated player list to all clients
        broadcastPlayerListToAll();
    }

}
