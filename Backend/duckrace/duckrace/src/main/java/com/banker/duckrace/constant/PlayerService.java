package com.banker.duckrace.constant;

import com.banker.duckrace.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
}
