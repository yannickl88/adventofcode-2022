package nl.yannickl88.adventofcode.days;

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
        private final int x;
        private final int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position move(Direction direction) {
            return switch (direction) {
                case R -> new Position(x + 1, y);
                case U -> new Position(x, y + 1);
                case D -> new Position(x, y - 1);
                case L -> new Position(x - 1, y);
            };
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
        Position head, tail;
        final ArrayList<Position> steps;

        public Rope() {
            head = new Position(0, 0);
            tail = new Position(0, 0);
            steps = new ArrayList<>();
            steps.add(tail);
        }

        void move(Direction direction, int steps) {
            System.out.printf("== %s %d ==\n", direction.name(), steps);

            for (int i = 0; i < steps; i++) {
                simulateMovementStep(direction);
                this.steps.add(tail);
            }
        }
        private void simulateMovementStep(Direction direction) {
            head = head.move(direction);
            int deltaX = tail.x - head.x;
            int deltaY = tail.y - head.y;

            if (Math.abs(deltaX) + Math.abs(deltaY) > 2) {
                if (deltaX < 0) {
                    if (deltaY < 0) {
                        tail = tail.move(Direction.U).move(Direction.R);
                    } else if (deltaY > 0) {
                        tail = tail.move(Direction.D).move(Direction.R);
                    }
                } else if (deltaX > 0) {
                    if (deltaY < 0) {
                        tail = tail.move(Direction.U).move(Direction.L);
                    } else if (deltaY > 0) {
                        tail = tail.move(Direction.D).move(Direction.L);
                    }
                }
            } else {
                if (deltaX < -1) {
                    tail = tail.move(Direction.R);
                } else if (deltaX > 1) {
                    tail = tail.move(Direction.L);
                }

                if (deltaY < -1) {
                    tail = tail.move(Direction.U);
                } else if (deltaY > 1) {
                    tail = tail.move(Direction.D);
                }
            }

//            System.out.printf("head is at (%d, %d), tail at (%d, %d)\n", head.x, head.y, tail.x, tail.y);
        }

        public Set<Position> getTailPosition() {
            return new HashSet<>(this.steps);
        }
    }

    public Day9() {
        File input = new File("inputs/day9/input.txt");
        Scanner moves;
        try {
            moves = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Rope rope = new Rope();

        while (moves.hasNext()) {
            String[] instruction = moves.nextLine().split(" ");
            rope.move(Direction.valueOf(instruction[0]), Integer.parseInt(instruction[1]));
        }

        Set<Position> positions = rope.getTailPosition();

        System.out.printf("The rope's tail visited %d position at least once.\n", positions.size());
    }
}
