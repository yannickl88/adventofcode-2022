package nl.yannickl88.adventofcode.util;

import java.util.HashMap;
import java.util.function.Function;

public class Grid<T> {
    private final T defaultValue;



    public static class Point {

        public final int x, y;
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
    final HashMap<Point, T> cells = new HashMap<>();

    public int minWidth = Integer.MAX_VALUE, minHeight = 0, maxWidth = 0, maxHeight = 0;
    public Grid(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Grid() {
        this.defaultValue = null;
    }

    public Point put(int x, int y, T type) {
        return put(new Point(x, y), type);
    }

    public Point put(Point point, T type) {
        cells.put(point, type);
        extendToAtLeast(point.x, point.y);

        return point;
    }
    public boolean inCurrentBounds(Point p) {
        return p.x >= minWidth && p.x <= maxWidth && p.y >= minHeight && p.y <= maxHeight;
    }

    public void extendToAtLeast(int x, int y) {
        minWidth = Math.min(minWidth, x);
        minHeight = Math.min(minHeight, y);
        maxWidth = Math.max(maxWidth, x);
        maxHeight = Math.max(maxHeight, y);
    }

    public T get(int x, int y) {
        return get(new Point(x, y));
    }

    public T get(Point point) {
        return cells.getOrDefault(point, defaultValue);
    }

    public void printAll(Function<T, String> elementPrinter) {
        print(elementPrinter, Math.min(0, minWidth), maxWidth, Math.min(0, minHeight), maxHeight);
    }
    public void print(Function<T, String> elementPrinter) {
        print(elementPrinter, minWidth, maxWidth, minHeight, maxHeight);
    }

    public void print(Function<T, String> elementPrinter, int minX, int maxX, int minY, int maxY) {
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                System.out.print(elementPrinter.apply(get(x, y)));
            }
            System.out.println();
        }
    }
}
