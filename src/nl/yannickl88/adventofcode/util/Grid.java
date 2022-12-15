package nl.yannickl88.adventofcode;

import java.util.HashMap;
import java.util.function.Function;

public class Grid<T> {
    private final T defaultValue;

    private static class Point {
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

    final private HashMap<Point, T> cells = new HashMap<>();
    int minWidth = Integer.MAX_VALUE, minHeight = 0, maxWidth = 0, maxHeight = 0;

    public Grid(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Grid() {
        this.defaultValue = null;
    }

    void put(int x, int y, T type) {
        cells.put(new Point(x, y), type);

        minWidth = Math.min(minWidth, x);
        minHeight = Math.min(minHeight, y);
        maxWidth = Math.max(maxWidth, x);
        maxHeight = Math.max(maxHeight, y);
    }

    T get(int x, int y) {
        return cells.getOrDefault(new Point(x, y), defaultValue);
    }

    public void print(Function<T, String> elementPrinter) {
        for (int y = minHeight; y <= maxHeight; y++) {
            for (int x = minWidth; x <= maxWidth; x++) {
                elementPrinter.apply(get(x, y));
            }
            System.out.println();
        }
    }
}
