package com.abhi.connect4.zip;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "zip_result",
       uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "seed"}))
public class ZipResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", nullable = false, length = 64)
    private String playerId;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    private long seed;

    @Column(name = "time_ms", nullable = false)
    private long timeMs;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    protected ZipResult() {}

    public ZipResult(String playerId, String nickname, long seed, long timeMs) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.seed = seed;
        this.timeMs = timeMs;
    }

    public String getPlayerId() { return playerId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public long getSeed() { return seed; }
    public long getTimeMs() { return timeMs; }
    public void setTimeMs(long timeMs) { this.timeMs = timeMs; }
}