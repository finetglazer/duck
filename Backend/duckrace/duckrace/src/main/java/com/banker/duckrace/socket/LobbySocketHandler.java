package com.banker.duckrace.socket;

import com.banker.duckrace.constant.PlayerService;
import com.banker.duckrace.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.UUID;

@Component
public class LobbySocketHandler extends TextWebSocketHandler {

    @Autowired
    private PlayerService playerService;



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Generate a unique player ID
        String playerId = UUID.randomUUID().toString();

        // When a new player connects, assign them a default name and points
        Player newPlayer = new Player("Player_" + playerId, 0);
        newPlayer.setId(playerId); // Set the player ID

        // Store the player with their unique ID
        playerService.getPlayerSessions().put(playerId, newPlayer);

        // Associate the WebSocket session with the player ID
        playerService.getSessionIdToPlayerIdMap().put(session.getId(), playerId);

        // Store the WebSocket session
        playerService.getWebSocketSessions().put(session.getId(), session);

        // Send the player ID back to the client
        session.sendMessage(new TextMessage("PLAYER_ID:" + playerId));

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
