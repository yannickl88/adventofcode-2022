package nl.yannickl88.adventofcode.days;

import nl.yannickl88.adventofcode.AlwaysScanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day9 {
    enum Direction {
        R,U,D,L
    }
    static class Position {
        private int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position move(Direction direction) {
            switch (direction) {
                case R -> x += 1;
                case U -> y += 1;
                case D -> y -= 1;
                case L -> x -= 1;
            }

            return this;
        }

        Position copy() {
            return new Position(x, y);
        }

        @Override
        public String toString() {
            return "(%d, %d)".formatted(x, y);
        }

        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Position && obj.hashCode() == this.hashCode();
        }
    }
    static class Rope {
        Position[] knots;

        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        final ArrayList<Position> steps;

        public Rope() {
            knots = new Position[]{
                    new Position(0, 0), // H
                    new Position(0, 0), // 1
                    new Position(0, 0), // 2
                    new Position(0, 0), // 3
                    new Position(0, 0), // 4
                    new Position(0, 0), // 5
                    new Position(0, 0), // 6
                    new Position(0, 0), // 7
                    new Position(0, 0), // 8
                    new Position(0, 0), // 9, tail
            };
            steps = new ArrayList<>();
        }

        void move(Direction direction, int steps) {
            System.out.printf("== %s %d ==\n", direction.name(), steps);

            for (int s = 0; s < steps; s++) {
                knots[0].move(direction);
                for (int i = 0; i < 9; i++) {
                    simulateMovementStep(knots[i], knots[i + 1]);
                }
//                print();
                this.steps.add(knots[9].copy());
            }
        }
        private void simulateMovementStep(Position head, Position tail) {
            int deltaX = tail.x - head.x;
            int deltaY = tail.y - head.y;

            if (Math.abs(deltaX) + Math.abs(deltaY) > 2) {
                if (deltaX < 0) {
                    if (deltaY < 0) {
                        tail.move(Direction.U).move(Direction.R);
                    } else if (deltaY > 0) {
                        tail.move(Direction.D).move(Direction.R);
                    }
                } else if (deltaX > 0) {
                    if (deltaY < 0) {
                        tail.move(Direction.U).move(Direction.L);
                    } else if (deltaY > 0) {
                        tail.move(Direction.D).move(Direction.L);
                    }
                }
            } else {
                if (deltaX < -1) {
                    tail.move(Direction.R);
                } else if (deltaX > 1) {
                    tail.move(Direction.L);
                }

                if (deltaY < -1) {
                    tail.move(Direction.U);
                } else if (deltaY > 1) {
                    tail.move(Direction.D);
                }
            }

            minX = Math.min(minX, head.x);
            minX = Math.min(minX, tail.x);
            maxX = Math.max(maxX, head.x);
            maxX = Math.max(maxX, tail.x);

            minY = Math.min(minY, head.y);
            minY = Math.min(minY, tail.y);
            maxY = Math.max(maxY, head.y);
            maxY = Math.max(maxY, tail.y);

//            System.out.printf("head is at (%d, %d), tail at (%d, %d)\n", head.x, head.y, tail.x, tail.y);
        }

        public Set<Position> getTailPosition() {
            return new HashSet<>(this.steps);
        }

        public void print() {
            System.out.println();

            for (int y = maxY; y >= minY; y--) {
                for (int x = minX; x <= maxX; x++) {
                    String symbol = ".";
                    for (int k = 0; k < 10; k++) {
                        if(knots[k].equals(new Position(x, y))) {
                            symbol = k == 0 ? "H" : String.valueOf(k);
                            break;
                        }
                    }

                    System.out.printf(symbol);
                }
                System.out.println();
            }
        }
    }

    public Day9() {
        AlwaysScanner moves = new AlwaysScanner(new File("inputs/day9/input.txt"));
        Rope rope = new Rope();
        rope.print();

        while (moves.hasNext()) {
            String[] instruction = moves.nextLine().split(" ");
            rope.move(Direction.valueOf(instruction[0]), Integer.parseInt(instruction[1]));
        }

        Set<Position> positions = rope.getTailPosition();

        System.out.printf("The rope's tail visited %d position at least once.\n", positions.size());
    }
}
