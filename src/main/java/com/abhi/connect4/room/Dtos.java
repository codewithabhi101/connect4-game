package com.abhi.connect4.room;

public class Dtos {
    public record JoinRequest(String playerId) {}
    public record MoveRequest(String playerId, int col) {}
    public record CreateResponse(String code) {}
    public record GameState(String code, int[][] board, int currentPlayer,
                            int winner, boolean draw, boolean ready) {}
    public record JoinResponse(String code, int playerNumber, GameState state) {}
}