package com.banker.duckrace.configuration;

import com.banker.duckrace.socket.DuckRaceSocketHandler;
import com.banker.duckrace.socket.LobbySocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private LobbySocketHandler lobbySocketHandler;

    @Autowired
    private DuckRaceSocketHandler duckRaceSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(lobbySocketHandler, "/duckRace")
                .setAllowedOrigins("*");  // Allow connections from any origin
        // New path for race events
        registry.addHandler(duckRaceSocketHandler, "/duckRace/race").setAllowedOrigins("*");
    }
}
