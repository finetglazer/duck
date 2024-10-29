package com.banker.duckrace.socket;

import com.banker.duckrace.constant.PlayerService;
import com.banker.duckrace.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
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

        // Assign default name and points
        Player newPlayer = new Player("Player_" + playerId, 1000);
        newPlayer.setId(playerId);

        // Store the player
        playerService.getPlayerSessions().put(playerId, newPlayer);

        // Associate session ID with player ID
        playerService.getSessionIdToPlayerIdMap().put(session.getId(), playerId);

        // Store session in lobbySessions
        playerService.getLobbySessions().put(session.getId(), session);

        // Send player ID to the client
        session.sendMessage(new TextMessage("PLAYER_ID:" + playerId));

        // Broadcast updated player list
        playerService.broadcastPlayerListToAll();
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        String sessionId = session.getId();
        String playerId = playerService.getSessionIdToPlayerIdMap().remove(sessionId);
        if (playerId != null) {
            playerService.getPlayerSessions().remove(playerId);
        }
        playerService.getLobbySessions().remove(sessionId);

        // Broadcast updated player list
        playerService.broadcastPlayerListToAll();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();

        // Get playerId from sessionId
        String playerId = playerService.getSessionIdToPlayerIdMap().get(session.getId());
        Player player = playerService.getPlayerSessions().get(playerId);

        if (player == null) {
            // Handle player not found
            return;
        }

        // Handle name change
        if (clientMessage.startsWith("SET_NAME:")) {
            String newName = clientMessage.split(":")[1];
            player.setName(newName);

            // Broadcast updated player data to all clients
            playerService.broadcastPlayerListToAll();
        }

        // Handle point update
        if (clientMessage.startsWith("ADD_POINTS:")) {
            int points = Integer.parseInt(clientMessage.split(":")[1]);
            player.addPoints(points);

            // Broadcast updated player data to all clients
            playerService.broadcastPlayerListToAll();
        }
    }



    // Broadcast the full player list to all connected clients
    private void broadcastPlayerListToAll() throws IOException {
        String playerListJson = new ObjectMapper().writeValueAsString(playerService.getPlayerSessions().values());

        // Send to lobby sessions only
        for (WebSocketSession clientSession : playerService.getLobbySessions().values()) {
            if (clientSession.isOpen()) {
                clientSession.sendMessage(new TextMessage(playerListJson));
            }
        }
    }


    // Optional: Add methods to handle other types of messages as needed
    // error handling, etc.

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        afterConnectionClosed(session, CloseStatus.SERVER_ERROR);
    }

}
