package com.banker.duckrace.constant;

import com.banker.duckrace.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class PlayerService {

    private Map<String, Player> playerSessions = new ConcurrentHashMap<>(); // Key: playerId
    private Map<String, String> sessionIdToPlayerIdMap = new ConcurrentHashMap<>();

    // Separate session maps for lobby and race room
    private Map<String, WebSocketSession> lobbySessions = new ConcurrentHashMap<>();
    private Map<String, WebSocketSession> raceSessions = new ConcurrentHashMap<>();

    public Map<String, Player> getPlayerSessions() {
        return playerSessions;
    }

    public Map<String, String> getSessionIdToPlayerIdMap() {
        return sessionIdToPlayerIdMap;
    }

    public Map<String, WebSocketSession> getLobbySessions() {
        return lobbySessions;
    }

    public Map<String, WebSocketSession> getRaceSessions() {
        return raceSessions;
    }

    public WebSocketSession getSessionByPlayerId(String playerId) {
        String sessionId = null;
        for (Map.Entry<String, String> entry : sessionIdToPlayerIdMap.entrySet()) {
            if (entry.getValue().equals(playerId)) {
                sessionId = entry.getKey();
                break;
            }
        }
        if (sessionId != null) {
            // Check raceSessions first
            WebSocketSession session = raceSessions.get(sessionId);
            if (session != null) {
                return session;
            }
            // Then check lobbySessions
            session = lobbySessions.get(sessionId);
            return session;
        }
        return null;
    }
    public void broadcastToAll(String message) {
        broadcastToLobby(message);
        broadcastToRace(message);
    }

    public void broadcastToLobby(String message) {
        for (WebSocketSession session : lobbySessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void broadcastToRace(String message) {
        for (WebSocketSession session : raceSessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public void broadcastPlayerUpdate() {
//        try {
//            Map<String, Object> message = new HashMap<>();
//            message.put("type", "playerUpdate");
//            message.put("players", playerSessions.values());
//            String playerUpdateJson = new ObjectMapper().writeValueAsString(message);
//
//            broadcastToAll(playerUpdateJson);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void broadcastPlayerListToAll() {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "playerUpdate");
            message.put("players", playerSessions.values());
            String playerListJson = new ObjectMapper().writeValueAsString(message);

            broadcastToAll(playerListJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
