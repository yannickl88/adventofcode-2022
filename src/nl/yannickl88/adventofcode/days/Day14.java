package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day14 {
    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return "%d,%d".formatted(x, y).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Point && ((Point) obj).x == x && ((Point) obj).y == y;
        }
    }

    static enum CellType {
        AIR, ROCK, SAND
    }

    static class Grid {
        final private HashMap<Point, CellType> cells = new HashMap<>();
        int minWidth = Integer.MAX_VALUE, minHeight = 0, maxWidth = 0, maxHeight = 0;

        void put(int x, int y, CellType type) {
            cells.put(new Point(x, y), type);

            minWidth = Math.min(minWidth, x);
            minHeight = Math.min(minHeight, y);
            maxWidth = Math.max(maxWidth, x);
            maxHeight = Math.max(maxHeight, y);
        }

        CellType get(int x, int y) {
            return cells.getOrDefault(new Point(x, y), CellType.AIR);
        }

        public void print() {
            for (int y = minHeight; y <= maxHeight; y++) {
                for (int x = minWidth; x <= maxWidth; x++) {
                    System.out.print(switch (get(x, y)) {
                        case AIR -> ".";
                        case ROCK -> "#";
                        case SAND -> "o";
                    });
                }
                System.out.println();
            }
        }
    }

    public Day14() {
        File input = new File("inputs/day14/input.txt");
        Scanner scans;
        try {
            scans = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Grid grid = new Grid();

        while (scans.hasNext()) {
            List<Point> points = Arrays.stream(scans.nextLine().split(" -> ")).map((line) -> {
                String[] point = line.split(",");

                return new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1]));
            }).collect(Collectors.toList());

            Point from = points.remove(0);

            while (points.size() > 0) {
                Point to = points.remove(0);

                if (from.x < to.x) {
                    for (int x = from.x; x <= to.x; x++) {
                        grid.put(x, from.y, CellType.ROCK);
                    }
                } else if (from.x > to.x) {
                    for (int x = to.x; x <= from.x; x++) {
                        grid.put(x, from.y, CellType.ROCK);
                    }
                } else {
                    if (from.y < to.y) {
                        for (int y = from.y; y <= to.y; y++) {
                            grid.put(from.x, y, CellType.ROCK);
                        }
                    } else if (from.y > to.y) {
                        for (int y = to.y; y <= from.y; y++) {
                            grid.put(from.x, y, CellType.ROCK);
                        }
                    }
                }

                from = to;
            }
        }

        int sandAdded = 0;
        int floor = grid.maxHeight;
        while (grid.get(500, 0) != CellType.SAND) {
            addSand(grid, floor);
            sandAdded++;
        }

        grid.print();

        System.out.println(sandAdded);
    }

    private boolean addSand(Grid grid, int floor) {
        Point sand = new Point(500, 0);

        while (true) {
            if (sand.y > floor) {
                grid.put(sand.x, sand.y, CellType.SAND);
                return true;
            }

            if (grid.get(sand.x, sand.y + 1) == CellType.AIR) { // Can I move down?
                sand.y += 1;
            } else if (grid.get(sand.x - 1, sand.y + 1) == CellType.AIR) { // Can I more diagonally left?
                sand.x -= 1;
                sand.y += 1;
            } else if (grid.get(sand.x + 1, sand.y + 1) == CellType.AIR) { // Can I more diagonally right?
                sand.x += 1;
                sand.y += 1;
            } else {
                grid.put(sand.x, sand.y, CellType.SAND);
                return true;
            }
        }
    }
}
