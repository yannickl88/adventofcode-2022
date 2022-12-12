package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {
    static class Grid {
        final List<List<Position>> cells;

        Grid(List<List<Position>> cells) {
            this.cells = cells;
        }

        int width() {
            return this.cells.get(0).size();
        }

        int height() {
            return this.cells.size();
        }

        public Position get(int x, int y) {
            return this.cells.get(y).get(x);
        }

        public List<Position> all() {
            return cells.stream().flatMap(List::stream).collect(Collectors.toList());
        }
    }

    static class Position {
        final Grid grid;
        final int x, y;
        final String height;

        public Position(Grid grid, int x, int y, String height) {
            this.grid = grid;
            this.x = x;
            this.y = y;
            this.height = height;
        }

        @Override
        public String toString() {
            return "(%d, %d, height: %s)".formatted(x, y, height);
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        public List<Position> getNeighbours() {
            ArrayList<Position> options = new ArrayList<>();

            if (x > 0) {
                addIfReachable(options, grid.get(x - 1, y));
            }
            if (x < grid.width() - 1) {
                addIfReachable(options, grid.get(x + 1, y));
            }
            if (y > 0) {
                addIfReachable(options, grid.get(x, y - 1));
            }
            if (y < grid.height() - 1) {
                addIfReachable(options, grid.get(x, y + 1));
            }

            return options;
        }

        int heightAsInt() {
            if (this.height.equals("S")) {
                return 0;
            }
            if (this.height.equals("E")) {
                return 26;
            }

            return this.height.charAt(0) - 96;
        }

        private void addIfReachable(ArrayList<Position> list, Position position) {
            if (position.heightAsInt() - this.heightAsInt() > 1) {
                return;
            }

            list.add(position);
        }
    }

    public Day12() {
        File input = new File("inputs/day12/input.txt");
        Scanner heightMap;
        try {
            heightMap = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<List<Position>> gridData = new ArrayList<>();
        Grid grid = new Grid(gridData);
        Position start = null, end = null;

        while (heightMap.hasNext()) {
            String line = heightMap.nextLine();
            List<Position> row = new ArrayList<>();

            for (int i = 0; i < line.length(); i++) {
                String index = line.substring(i, i + 1);
                Position position = new Position(grid, row.size(), gridData.size(), index);

                row.add(position);

                if (index.equals("S")) {
                    start = position;
                }
                if (index.equals("E")) {
                    end = position;
                }
            }

            gridData.add(row);
        }

        List<Position> path = getShortestPath(start, end);

        System.out.printf("fewest steps to destination is %d\n", path.size() - 1);
        System.out.println(path);
    }

    List<Position> getShortestPath(Position from, Position to) {
        Map<Position, Integer> distances = new HashMap<>();
        Map<Position, Position> previous = new HashMap<>();
        List<Position> queue = new ArrayList<>();
        queue.add(from);

        distances.put(from, 0);

        while (!queue.isEmpty()) {
            queue.sort(Comparator.comparingInt(a -> distances.getOrDefault(a, Integer.MAX_VALUE)));

            Position u = queue.remove(0);
            if (u == to) {
                break;
            }

            for (Position v : u.getNeighbours()) {
                int alt = distances.get(u) + 1;
                if (alt < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                    distances.put(v, alt);
                    previous.put(v, u);
                    queue.add(v);
                }
            }
        }

        List<Position> path = new ArrayList<>();
        Position current = to;

        while (current != null) {
            path.add(0, current);
            if (current == from) {
                break;
            }

            current = previous.getOrDefault(current, from);
        }

        return path;
    }
}
