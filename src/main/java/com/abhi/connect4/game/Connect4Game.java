package com.abhi.connect4.game;

public class Connect4Game {

    public static final int ROWS = 6;
    public static final int COLS = 7;

    private final int[][] board = new int[ROWS][COLS];
    private int currentPlayer = 1;
    private int winner = 0;
    private int moves = 0;

    public synchronized int drop(int player, int col) {
        if (winner != 0 || isDraw()) {
            throw new IllegalStateException("Game is over");
        }
        if (player != currentPlayer) {
            throw new IllegalStateException("Not your turn");
        }
        if (col < 0 || col >= COLS) {
            throw new IllegalArgumentException("Invalid column");
        }

        int row = -1;
        for (int r = ROWS - 1; r >= 0; r--) {
            if (board[r][col] == 0) {
                row = r;
                break;
            }
        }
        if (row == -1) {
            throw new IllegalArgumentException("Column is full");
        }

        board[row][col] = player;
        moves++;

        if (checkWin(row, col, player)) {
            winner = player;
        } else {
            currentPlayer = (player == 1) ? 2 : 1;
        }
        return row;
    }

    private boolean checkWin(int row, int col, int player) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] d : directions) {
            int count = 1 + countDirection(row, col, d[0], d[1], player)
                          + countDirection(row, col, -d[0], -d[1], player);
            if (count >= 4) {
                return true;
            }
        }
        return false;
    }

    private int countDirection(int row, int col, int dr, int dc, int player) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == player) {
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }

    public boolean isDraw() {
        return winner == 0 && moves == ROWS * COLS;
    }

    public int getWinner() {
        return winner;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[][] getBoard() {
        int[][] copy = new int[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            copy[r] = board[r].clone();
        }
        return copy;
    }
}