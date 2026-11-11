package com.abhi.connect4.zip;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ZipResultRepository extends JpaRepository<ZipResult, Long> {

    Optional<ZipResult> findByPlayerIdAndSeed(String playerId, long seed);

    List<ZipResult> findTop10BySeedOrderByTimeMsAsc(long seed);

    List<ZipResult> findByPlayerIdOrderBySeedDesc(String playerId);
}