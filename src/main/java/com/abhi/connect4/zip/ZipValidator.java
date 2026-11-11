package com.abhi.connect4.zip;

import java.util.List;

public final class ZipValidator {

    private ZipValidator() {}

    public static boolean isValid(ZipPuzzle puzzle, List<Integer> path) {
        int size = puzzle.size();
        int total = size * size;
        List<Integer> checkpoints = puzzle.checkpoints();

        if (path == null || path.size() != total) {
            return false;
        }
        if (!path.get(0).equals(checkpoints.get(0))) {
            return false;
        }

        boolean[] seen = new boolean[total];
        for (int i = 0; i < path.size(); i++) {
            Integer cell = path.get(i);
            if (cell == null || cell < 0 || cell >= total || seen[cell]) {
                return false;
            }
            seen[cell] = true;
            if (i > 0 && !ZipGenerator.neighbors(path.get(i - 1), size).contains(cell)) {
                return false;
            }
        }

        int next = 0;
        for (int cell : path) {
            int k = checkpoints.indexOf(cell);
            if (k >= 0) {
                if (k != next) {
                    return false;
                }
                next++;
            }
        }
        return next == checkpoints.size()
                && path.get(total - 1).equals(checkpoints.get(checkpoints.size() - 1));
    }
}