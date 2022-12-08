package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day8 {
    static class Tree {
        private final int height, x, y;

        Tree(int height, int x, int y) {
            this.height = height;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "tree[height=%s,x=%d,y=%d]".formatted(height, x, y);
        }
    }
    static class Grid {
        private final Tree[] trees;
        private final int size;

        Grid(Tree[] trees, int size) {
            this.trees = trees;
            this.size = size;
        }

        List<Tree> getVisibleTrees() {
            ArrayList<Tree> visible = new ArrayList<>();

            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (isVisible(x, y)) {
                        visible.add(trees[x + y * size]);
                    }
                }
            }

            return visible;
        }

        boolean isVisible(int x, int y) {
            // Outer trees
            if (x == 0 || x == size - 1 || y == 0 || y == size - 1) {
                return true;
            }

            // Inner trees
            return isVisibleHorizontal(x, y) || isVisibleVertical(x, y);
        }

        boolean isVisibleHorizontal(int x, int y) {
            int referenceHeight = trees[x + y * size].height;
            boolean visible = true;

            // from the left
            for (int i = x - 1; i >= 0; i--) {
                int height = trees[i + y * size].height;
                if (referenceHeight <= height) {
                    visible = false;
                    break;
                }
            }

            if (visible) {
                return true;
            }

            // Reset it back, now check from right.
            visible = true;

            // from the right
            for (int i = x + 1; i < size; i++) {
                int height = trees[i + y * size].height;
                if (referenceHeight <= height) {
                    visible = false;
                    break;
                }
            }

            return visible;
        }

        private boolean isVisibleVertical(int x, int y) {
            int referenceHeight = trees[x + y * size].height;
            boolean visible = true;

            // from the top
            for (int i = y - 1; i >= 0; i--) {
                int height = trees[x + i * size].height;
                if (referenceHeight <= height) {
                    visible = false;
                    break;
                }
            }

            if (visible) {
                return true;
            }

            // Reset it back, now check from bottom.
            visible = true;

            // from the bottom
            for (int i = y + 1; i < size; i++) {
                int height = trees[x + i * size].height;
                if (referenceHeight <= height) {
                    visible = false;
                    break;
                }
            }

            return visible;
        }
    }

    public Day8() {
        File input = new File("inputs/day8/input.txt");
        Scanner droneInfo;
        try {
            droneInfo = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String row = droneInfo.nextLine();
        int gridSize = row.length();
        Tree[] trees = new Tree[gridSize * gridSize];

        insertTrees(row, trees, 0, gridSize);
        int offset = gridSize;

        while (droneInfo.hasNext()) {
            insertTrees(droneInfo.nextLine(), trees, offset, gridSize);
            offset += gridSize;
        }

        Grid grid = new Grid(trees, gridSize);
        List<Tree> visible = grid.getVisibleTrees();

        System.out.println(visible);
        System.out.printf("%d trees are visible from outside the grid\n", visible.size());
    }

    private void insertTrees(String trees, Tree[] grid, int offset, int gridSize) {
        for (String height : trees.split("")) {
            grid[offset] = new Tree(Integer.parseInt(height), offset % gridSize, offset / gridSize );

            offset++;
        }
    }
}
