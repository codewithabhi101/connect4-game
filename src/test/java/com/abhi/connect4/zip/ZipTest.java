package com.abhi.connect4.zip;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

class ZipTest {

    @Test
    void puzzleHasDistinctCheckpoints() {
        ZipPuzzle p = ZipGenerator.generate(42, 5, 6);
        assertEquals(6, p.checkpoints().size());
        assertEquals(6, new HashSet<>(p.checkpoints()).size());
    }

    @Test
    void sameSeedGivesSamePuzzle() {
        assertEquals(ZipGenerator.generate(7, 5, 6), ZipGenerator.generate(7, 5, 6));
    }

    @Test
    void generatedSolutionIsValid() {
        for (long seed = 1; seed <= 50; seed++) {
            ZipGenerator.Generated g = ZipGenerator.generateFull(seed, 5, 6);
            assertTrue(ZipValidator.isValid(g.puzzle(), g.solution()), "seed " + seed);
        }
    }

    @Test
    void reversedPathIsInvalid() {
        ZipGenerator.Generated g = ZipGenerator.generateFull(3, 5, 6);
        List<Integer> reversed = new ArrayList<>(g.solution());
        Collections.reverse(reversed);
        assertFalse(ZipValidator.isValid(g.puzzle(), reversed));
    }

    @Test
    void shortPathIsInvalid() {
        ZipGenerator.Generated g = ZipGenerator.generateFull(3, 5, 6);
        List<Integer> shortPath = new ArrayList<>(g.solution());
        shortPath.remove(shortPath.size() - 1);
        assertFalse(ZipValidator.isValid(g.puzzle(), shortPath));
    }
}