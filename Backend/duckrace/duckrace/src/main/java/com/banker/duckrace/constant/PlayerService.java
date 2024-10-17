package com.banker.duckrace.constant;

import com.banker.duckrace.model.Player;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;

@Service
public class PlayerService {

    // Shared map to store player sessions
    private ConcurrentHashMap<String, Player> playerSessions = new ConcurrentHashMap<>();

    // Shared map to store WebSocket sessions
    private ConcurrentHashMap<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Player> getPlayerSessions() {
        return playerSessions;
    }

    public ConcurrentHashMap<String, WebSocketSession> getWebSocketSessions() {
        return webSocketSessions;
    }

    // Optional: Add methods to add/remove players and WebSocket sessions as needed
}
