package com.abhi.connect4.room;

import com.abhi.connect4.game.Connect4Game;

public class Room {

    private final String code;
    private final Connect4Game game = new Connect4Game();
    private String player1;
    private String player2;

    public Room(String code) {
        this.code = code;
    }

    public synchronized int join(String playerId) {
        if (playerId == null || playerId.isBlank()) {
            throw new IllegalArgumentException("Missing player id");
        }
        if (playerId.equals(player1)) return 1;
        if (playerId.equals(player2)) return 2;
        if (player1 == null) { player1 = playerId; return 1; }
        if (player2 == null) { player2 = playerId; return 2; }
        throw new IllegalStateException("Room is full");
    }

    public synchronized void move(String playerId, int col) {
        if (playerId == null) {
            throw new IllegalArgumentException("Missing player id");
        }
        if (player2 == null) {
            throw new IllegalStateException("Waiting for opponent");
        }
        int player = playerId.equals(player1) ? 1 : playerId.equals(player2) ? 2 : 0;
        if (player == 0) {
            throw new IllegalStateException("You are not in this room");
        }
        game.drop(player, col);
    }

    public synchronized Dtos.GameState state() {
        return new Dtos.GameState(code, game.getBoard(), game.getCurrentPlayer(),
                game.getWinner(), game.isDraw(), player2 != null);
    }
}