package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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

        int getHighestScenicScore() {
            int highestScore = 0;

            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    int score = getScenicScore(x, y);
                    if (score > highestScore) {
                        highestScore = score;
                    }
                }
            }

            return highestScore;
        }

        int getScenicScore(int x, int y) {
            int referenceHeight = trees[x + y * size].height;
            int[] scenicScoreComponents = new int[] {0, 0, 0, 0};

            // from the left
            for (int i = x - 1; i >= 0; i--) {
                int height = trees[i + y * size].height;
                scenicScoreComponents[1]++;
                if (referenceHeight <= height) {
                    break;
                }
            }

            // from the right
            for (int i = x + 1; i < size; i++) {
                int height = trees[i + y * size].height;
                scenicScoreComponents[2]++;
                if (referenceHeight <= height) {
                    break;
                }
            }

            // from the top
            for (int i = y - 1; i >= 0; i--) {
                int height = trees[x + i * size].height;
                scenicScoreComponents[0]++;
                if (referenceHeight <= height) {
                    break;
                }
            }

            // from the bottom
            for (int i = y + 1; i < size; i++) {
                int height = trees[x + i * size].height;
                scenicScoreComponents[3]++;
                if (referenceHeight <= height) {
                    break;
                }
            }

            return Arrays.stream(scenicScoreComponents).reduce(1, (acc, value) -> acc * value);
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

        System.out.printf("%d is the highest scenic score possible for any tree\n", grid.getHighestScenicScore());
    }

    private void insertTrees(String trees, Tree[] grid, int offset, int gridSize) {
        for (String height : trees.split("")) {
            grid[offset] = new Tree(Integer.parseInt(height), offset % gridSize, offset / gridSize );

            offset++;
        }
    }
}
