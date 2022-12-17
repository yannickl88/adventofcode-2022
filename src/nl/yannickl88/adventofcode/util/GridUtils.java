package nl.yannickl88.adventofcode.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GridUtils {
    public static <T> List<Grid.Point> findAll(Grid<T> grid, T type) {
        ArrayList<Grid.Point> points = new ArrayList<>();

        for (Map.Entry<Grid.Point, T> e : grid.cells.entrySet()) {
            if (e.getValue().equals(type)) {
                points.add(e.getKey());
            }
        }

        return points;
    }
    public static <T> List<T> getRow(Grid<T> grid, int y) {
        ArrayList<T> points = new ArrayList<>();

        for (int x = grid.minWidth; x <= grid.maxWidth; x++) {
            points.add(grid.get(x, y));
        }

        return points;
    }

    public static <T> int distanceManhattan (Grid.Point a, Grid.Point b) {
        return distanceManhattan(a.x, a.y, b.x, b.y);
    }

    public static <T> int distanceManhattan (int ax, int ay, int bx, int by) {
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }
}
