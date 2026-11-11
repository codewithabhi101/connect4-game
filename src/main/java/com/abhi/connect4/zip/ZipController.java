package com.abhi.connect4.zip;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zip")
public class ZipController {

    private final ZipService service;

    public ZipController(ZipService service) {
        this.service = service;
    }

    @GetMapping("/daily")
    public ZipDtos.PuzzleResponse daily() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        return build(today.toEpochDay(), "Daily " + today);
    }

    @GetMapping("/random")
    public ZipDtos.PuzzleResponse random() {
        long seed = ThreadLocalRandom.current().nextLong(1_000_000, 1_000_000_000L);
        return build(seed, "Random #" + seed);
    }

    @PostMapping("/solve")
    public ZipDtos.SolveResponse solve(@RequestBody ZipDtos.SolveRequest req) {
        return service.solve(req);
    }

    @GetMapping("/leaderboard")
    public List<ZipDtos.LeaderboardEntry> leaderboard() {
        return service.leaderboard();
    }

    @GetMapping("/streak")
    public ZipDtos.StreakResponse streak(@RequestParam("playerId") String playerId) {
        return new ZipDtos.StreakResponse(service.streak(playerId));
    }

    private ZipDtos.PuzzleResponse build(long seed, String label) {
        ZipPuzzle p = ZipGenerator.generate(seed, ZipService.SIZE, ZipService.CHECKPOINTS);
        return new ZipDtos.PuzzleResponse(seed, label, p.size(), p.checkpoints());
    }
}