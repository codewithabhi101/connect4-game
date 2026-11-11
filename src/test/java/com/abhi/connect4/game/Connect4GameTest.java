package com.abhi.connect4.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Connect4GameTest {

    @Test
    void horizontalWin() {
        Connect4Game g = new Connect4Game();
        g.drop(1, 0); g.drop(2, 0);
        g.drop(1, 1); g.drop(2, 1);
        g.drop(1, 2); g.drop(2, 2);
        g.drop(1, 3);
        assertEquals(1, g.getWinner());
    }

    @Test
    void verticalWin() {
        Connect4Game g = new Connect4Game();
        g.drop(1, 0); g.drop(2, 1);
        g.drop(1, 0); g.drop(2, 1);
        g.drop(1, 0); g.drop(2, 1);
        g.drop(1, 0);
        assertEquals(1, g.getWinner());
    }

    @Test
    void diagonalWin() {
        Connect4Game g = new Connect4Game();
        g.drop(1, 0);
        g.drop(2, 1); g.drop(1, 1);
        g.drop(2, 2); g.drop(1, 6); g.drop(2, 2); g.drop(1, 2);
        g.drop(2, 3); g.drop(1, 3); g.drop(2, 3); g.drop(1, 3);
        assertEquals(1, g.getWinner());
    }

    @Test
    void wrongTurnThrows() {
        Connect4Game g = new Connect4Game();
        assertThrows(IllegalStateException.class, () -> g.drop(2, 0));
    }

    @Test
    void fullColumnThrows() {
        Connect4Game g = new Connect4Game();
        int p = 1;
        for (int i = 0; i < 6; i++) {
            g.drop(p, 0);
            p = (p == 1) ? 2 : 1;
        }
        int next = p;
        assertThrows(IllegalArgumentException.class, () -> g.drop(next, 0));
    }
}