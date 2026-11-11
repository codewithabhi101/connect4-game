package com.abhi.connect4.zip;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ZipService {

    public static final int SIZE = 5;
    public static final int CHECKPOINTS = 6;

    private final ZipResultRepository repo;

    public ZipService(ZipResultRepository repo) {
        this.repo = repo;
    }

    public static long todaySeed() {
        return LocalDate.now(ZoneOffset.UTC).toEpochDay();
    }

    public ZipDtos.SolveResponse solve(ZipDtos.SolveRequest req) {
        ZipPuzzle puzzle = ZipGenerator.generate(req.seed(), SIZE, CHECKPOINTS);
        boolean valid = ZipValidator.isValid(puzzle, req.path());
        int streak = 0;

        String playerId = req.playerId();
        if (valid && req.seed() == todaySeed() && playerId != null && !playerId.isBlank()) {
            String nick = cleanName(req.nickname());
            long time = Math.max(req.timeMs(), 1000);

            repo.findByPlayerIdAndSeed(playerId, req.seed()).ifPresentOrElse(existing -> {
                if (time < existing.getTimeMs()) {
                    existing.setTimeMs(time);
                    existing.setNickname(nick);
                    repo.save(existing);
                }
            }, () -> repo.save(new ZipResult(playerId, nick, req.seed(), time)));

            streak = streak(playerId);
        }
        return new ZipDtos.SolveResponse(valid, streak);
    }

    public int streak(String playerId) {
        if (playerId == null || playerId.isBlank()) {
            return 0;
        }
        Set<Long> days = repo.findByPlayerIdOrderBySeedDesc(playerId).stream()
                .map(ZipResult::getSeed)
                .collect(Collectors.toSet());
        long today = todaySeed();
        long day = days.contains(today) ? today : today - 1;
        int count = 0;
        while (days.contains(day)) {
            count++;
            day--;
        }
        return count;
    }

    public List<ZipDtos.LeaderboardEntry> leaderboard() {
        return repo.findTop10BySeedOrderByTimeMsAsc(todaySeed()).stream()
                .map(r -> new ZipDtos.LeaderboardEntry(r.getNickname(), r.getTimeMs()))
                .toList();
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            return "Player";
        }
        String n = name.trim();
        return n.length() > 20 ? n.substring(0, 20) : n;
    }
}