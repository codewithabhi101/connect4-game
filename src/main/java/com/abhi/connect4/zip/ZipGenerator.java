package com.abhi.connect4.zip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public final class ZipGenerator {

    private ZipGenerator() {}

    public record Generated(ZipPuzzle puzzle, List<Integer> solution) {}

    public static ZipPuzzle generate(long seed, int size, int checkpointCount) {
        return generateFull(seed, size, checkpointCount).puzzle();
    }

    public static Generated generateFull(long seed, int size, int checkpointCount) {
        Random rnd = new Random(seed);
        List<Integer> path = randomPath(rnd, size);
        int total = size * size;

        TreeSet<Integer> indexes = new TreeSet<>();
        indexes.add(0);
        indexes.add(total - 1);
        while (indexes.size() < checkpointCount) {
            indexes.add(1 + rnd.nextInt(total - 2));
        }

        List<Integer> checkpoints = new ArrayList<>();
        for (int i : indexes) {
            checkpoints.add(path.get(i));
        }
        return new Generated(new ZipPuzzle(size, checkpoints), path);
    }

    private static List<Integer> randomPath(Random rnd, int size) {
        int total = size * size;
        while (true) {
            int start = rnd.nextInt(total);
            boolean wrongParity = ((start / size) + (start % size)) % 2 != 0;
            if (size % 2 == 1 && wrongParity) {
                continue;
            }
            boolean[] visited = new boolean[total];
            List<Integer> path = new ArrayList<>();
            path.add(start);
            visited[start] = true;
            if (dfs(path, visited, size, rnd, new int[] {0})) {
                return path;
            }
        }
    }

    private static boolean dfs(List<Integer> path, boolean[] visited, int size,
                               Random rnd, int[] steps) {
        if (path.size() == size * size) {
            return true;
        }
        if (++steps[0] > 20000) {
            return false;
        }
        List<Integer> options = neighbors(path.get(path.size() - 1), size);
        options.removeIf(n -> visited[n]);
        Collections.shuffle(options, rnd);
        for (int next : options) {
            visited[next] = true;
            path.add(next);
            if (dfs(path, visited, size, rnd, steps)) {
                return true;
            }
            path.remove(path.size() - 1);
            visited[next] = false;
        }
        return false;
    }

    public static List<Integer> neighbors(int cell, int size) {
        int r = cell / size;
        int c = cell % size;
        List<Integer> list = new ArrayList<>();
        if (r > 0) list.add(cell - size);
        if (r < size - 1) list.add(cell + size);
        if (c > 0) list.add(cell - 1);
        if (c < size - 1) list.add(cell + 1);
        return list;
    }
}