package com.banker.duckrace.constant;

import com.banker.duckrace.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;

@Service
public class PlayerService {

    // Shared map to store player sessions
    private ConcurrentHashMap<String, Player> playerSessions = new ConcurrentHashMap<>();

    // Shared map to store WebSocket sessions
    private ConcurrentHashMap<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

    private Map<String, String> sessionIdToPlayerIdMap = new ConcurrentHashMap<>();

    public Map<String, String> getSessionIdToPlayerIdMap() {
        return sessionIdToPlayerIdMap;
    }

    public ConcurrentHashMap<String, Player> getPlayerSessions() {
        return playerSessions;
    }

    public ConcurrentHashMap<String, WebSocketSession> getWebSocketSessions() {
        return webSocketSessions;
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
            return webSocketSessions.get(sessionId);
        }
        return null;
    }


    // Optional: Add methods to add/remove players and WebSocket sessions as needed
}
