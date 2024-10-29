package com.banker.duckrace.socket;

import com.banker.duckrace.constant.PlayerService;
import com.banker.duckrace.model.Bet;
import com.banker.duckrace.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.springframework.context.event.EventListener;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DuckRaceSocketHandler extends TextWebSocketHandler {

    @Autowired
    private PlayerService playerService;

    // Map to store bets for the current race: candidateId -> List of bets
    private Map<Integer, List<Bet>> candidateBets = new ConcurrentHashMap<>();

    // Total bet pool amount
    private int totalBetPool = 0;

    // Race status flags
    private boolean isBettingOpen = false;
    private boolean isRaceInProgress = false;

    // Pre-determined winner for the race
    private int winningCandidateId;

    // Initialize the game loop when the server starts
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        try {
            handleStartBetting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String playerId = getPlayerIdFromSession(session);

        if (playerId != null && playerService.getPlayerSessions().containsKey(playerId)) {
            // Associate session ID with player ID
            playerService.getSessionIdToPlayerIdMap().put(session.getId(), playerId);

            // Store session in raceSessions
            playerService.getRaceSessions().put(session.getId(), session);

            // Send confirmation
            //add points to player
            session.sendMessage(new TextMessage("{\"type\":\"connectedToRaceRoom\"}"));
            session.sendMessage(new TextMessage("{\"type\":\"points\",\"points\":" + playerService.getPlayerSessions().get(playerId).getPoints() + "}"));
        } else {
            // Invalid player ID
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"Invalid player ID\"}"));
            session.close();
        }
    }

    private String getPlayerIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2 && "playerId".equals(pair[0])) {
                    return pair[1];
                }
            }
        }
        return null;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            System.out.println(message.getPayload());
            String payload = message.getPayload();
            session.sendMessage(new TextMessage("{\"type\":\"echo\",\"message\":\"" + payload + "\"}"));

            // Parse the incoming message as JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> messageMap = mapper.readValue(payload, Map.class);

            String type = (String) messageMap.get("type");

            // Retrieve the player ID associated with this session
            String playerId = playerService.getSessionIdToPlayerIdMap().get(session.getId());
            Player player = playerService.getPlayerSessions().get(playerId);

            if (player == null) {
                session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"Player not found\"}"));
                return;
            }

            switch (type) {
                case "placeBet":

                    int candidateId = Integer.parseInt((String) messageMap.get("candidateId"));
                    int amount = Integer.parseInt((String) messageMap.get("amount"));
                    handlePlaceBet(player, candidateId, amount, session);
                    break;
                default:
                    session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"Unknown message type\"}"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"Invalid message format\"}"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    private synchronized void handleStartBetting() throws IOException {
        if (isBettingOpen || isRaceInProgress) {
            // Betting is already open or race is in progress
            return;
        }

        // Open betting
        isBettingOpen = true;
        candidateBets.clear();

        // Broadcast to all players that betting has started
        String message = "{\"type\":\"bettingStarted\",\"message\":\"Betting has started! You have 30 seconds to place your bets.\",\"totalPool\":" + totalBetPool + "}";
        broadcastToAll(message);

        // Start a countdown timer for betting (e.g., 30 seconds)
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int remainingTime = 30;

            @Override
            public void run() {
                if (remainingTime > 0) {
                    try {
                        String timerMessage = "{\"type\":\"timer\",\"remainingTime\":" + remainingTime + "}";
                        broadcastToAll(timerMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    remainingTime--;
                } else {
                    this.cancel();
                    try {
                        handleStartRace();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1000); // Every 1 second
    }

    private void handlePlaceBet(Player player, int candidateId, int amount, WebSocketSession session) throws IOException {
        if (!isBettingOpen) {
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"Betting is closed\"}"));
            return;
        }

        // Validate candidate ID
        if (candidateId < 1 || candidateId > 5) {
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"Invalid candidate ID\"}"));
            return;
        }

        // Validate bet amount
        if (amount <= 0) {
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"Bet amount must be positive\"}"));
            return;
        }

        if (player.getPoints() < amount) {
            session.sendMessage(new TextMessage("{\"type\":\"error\",\"message\":\"Insufficient points\"}"));
            return;
        }

        // Deduct the bet amount from the player's points
        synchronized (player) {
            player.setPoints(player.getPoints() - amount);
        }

        // Broadcast updated player data to all clients
        playerService.broadcastPlayerUpdate(player);

        // Create a new bet
        Bet bet = new Bet(player.getId(), candidateId, amount);

        // Add the bet to the candidate's list of bets
        candidateBets.computeIfAbsent(candidateId, k -> Collections.synchronizedList(new ArrayList<>())).add(bet);

        // Update the total bet pool
        synchronized (this) {
            totalBetPool += amount;
        }

        // Send confirmation to the player
        session.sendMessage(new TextMessage("{\"type\":\"betPlaced\",\"message\":\"Bet placed successfully\"}"));
    }

    private void handleStartRace() throws IOException, InterruptedException {
        isBettingOpen = false;
        isRaceInProgress = true;

        // Randomly select a winning candidate
        winningCandidateId = new Random().nextInt(5) + 1; // Candidate IDs are from 1 to 5

        // Simulate the race using threads
        simulateRace();

        // After race is over, calculate rewards
        calculateRewards();
    }

    private void simulateRace() throws IOException, InterruptedException {
        // Broadcast to all players that the race has started
        broadcastToAll("{\"type\":\"raceStarted\",\"message\":\"The race has started!\"}");

        // Create threads for each candidate
        Map<Integer, Integer> candidatePositions = new ConcurrentHashMap<>();
        List<Thread> threads = new ArrayList<>();

        // Use a flag to indicate when the race is over
        final boolean[] raceOver = {false};

        for (int i = 1; i <= 5; i++) {
            final int candidateId = i;
            Thread thread = new Thread(() -> {
                int position = 0;
                Random rand = new Random();

                // Simulate movement until a candidate reaches the finish line or race is over
                while (position < 100 && !raceOver[0]) {
                    // Move randomly between 1 to 10 units
                    position += rand.nextInt(10) + 1;

                    // Update the position
                    candidatePositions.put(candidateId, position);

                    // Send race update to all players
                    try {
                        Map<String, Object> updateMessage = new HashMap<>();
                        updateMessage.put("type", "raceUpdate");
                        updateMessage.put("candidateId", candidateId);
                        updateMessage.put("position", position);
                        String message = new ObjectMapper().writeValueAsString(updateMessage);
                        broadcastToAll(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Sleep for a short time to simulate time passing
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // Thread interrupted
                        Thread.currentThread().interrupt();
                        break;
                    }

                    // Check if the winner has reached the finish line
                    if (candidatePositions.getOrDefault(winningCandidateId, 0) >= 100) {
                        raceOver[0] = true;
                        break;
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // Announce the winner
        Map<String, Object> resultMessage = new HashMap<>();
        resultMessage.put("type", "raceFinished");
        resultMessage.put("winner", winningCandidateId);
        String message = new ObjectMapper().writeValueAsString(resultMessage);
        broadcastToAll(message);
    }

    private void calculateRewards() throws IOException {
        // Get the list of bets on the winning candidate
        List<Bet> winningBets = candidateBets.getOrDefault(winningCandidateId, Collections.emptyList());

        int totalWinningBets = winningBets.stream().mapToInt(Bet::getAmount).sum();

        // If there are winners, distribute the rewards
        if (totalWinningBets > 0) {
            for (Bet bet : winningBets) {
                Player player = playerService.getPlayerSessions().get(bet.getPlayerId());
                if (player != null) {
                    // Calculate reward
                    long reward = (long) bet.getAmount() * totalBetPool / totalWinningBets;

                    // Update player's points
                    synchronized (player) {
                        player.addPoints((int) reward);
                    }

                    // Notify the player
                    Map<String, Object> rewardMessage = new HashMap<>();
                    rewardMessage.put("type", "reward");
                    rewardMessage.put("reward", (int) reward);
                    rewardMessage.put("message", "You won " + reward + " points!");
                    String rewardMsg = new ObjectMapper().writeValueAsString(rewardMessage);

                    // Send message to the player
                    WebSocketSession playerSession = playerService.getSessionByPlayerId(player.getId());
                    if (playerSession != null && playerSession.isOpen()) {
                        playerSession.sendMessage(new TextMessage(rewardMsg));
                    }

                    // Broadcast updated player data to all clients
                    playerService.broadcastPlayerUpdate(player);
                }
            }
            // Reset the total bet pool after distributing rewards
            totalBetPool = 0;
        } else {
            // No winners, total pool carries over to the next race
            broadcastToAll("{\"type\":\"noWinners\",\"message\":\"No winners this race. Pool carries over to next race.\"}");
            // totalBetPool remains unchanged
        }

        // Prepare for the next race
        candidateBets.clear();
        isRaceInProgress = false;

        // Start the next betting phase automatically
        handleStartBetting();
    }

    private void broadcastToAll(String message) throws IOException {
        // Send to race sessions only
        for (WebSocketSession session : playerService.getRaceSessions().values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        playerService.getSessionIdToPlayerIdMap().remove(sessionId);
        playerService.getRaceSessions().remove(sessionId);
        // Optionally handle player status
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        afterConnectionClosed(session, CloseStatus.SERVER_ERROR);
    }
}
