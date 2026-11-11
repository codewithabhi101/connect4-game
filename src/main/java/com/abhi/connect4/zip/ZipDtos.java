package com.abhi.connect4.zip;

import java.util.List;

public class ZipDtos {
    public record PuzzleResponse(long seed, String label, int size, List<Integer> checkpoints) {}
    public record SolveRequest(long seed, List<Integer> path, String playerId,
                               String nickname, long timeMs) {}
    public record SolveResponse(boolean valid, int streak) {}
    public record LeaderboardEntry(String nickname, long timeMs) {}
    public record StreakResponse(int streak) {}
}