package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Day13 {
    static class Segment {
        Integer value;

        ArrayList<Segment> segments = new ArrayList<>();

        private Segment() {
        }
        private Segment(int value) {
            this.value = value;
        }
        private Segment(Segment nest) {
            this.segments.add(nest);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (null != value) {
                builder.append(value);
            } else {
                builder.append('[');

                for (int i = 0; i < segments.size(); i++) {
                    if (i > 0) {
                        builder.append(',');
                    }
                    builder.append(segments.get(i).toString());
                }

                builder.append(']');
            }

            return builder.toString();
        }

        static Segment parse(String data) {
            data = data.substring(1, data.length() - 1);

            Stack<Segment> stack = new Stack<>();
            Segment current = new Segment();
            int lastCursor = 0;
            int cursor = 0;

            while (cursor < data.length()) {
                if (data.charAt(cursor) == '[') {
                    stack.add(current);
                    current = new Segment();

                    cursor++;
                    lastCursor = cursor;
                } else if (data.charAt(cursor) == ']') {
                    if (lastCursor < cursor) {
                        int number = Integer.parseInt(data.substring(lastCursor, cursor));
                        current.segments.add(new Segment(number));
                    }

                    Segment nested = current;
                    current = stack.pop();
                    current.segments.add(nested);

                    cursor++;
                    lastCursor = cursor;
                } else if (data.charAt(cursor) == ',') {
                    if (lastCursor < cursor) {
                        int number = Integer.parseInt(data.substring(lastCursor, cursor));
                        current.segments.add(new Segment(number));
                    }

                    cursor++;
                    lastCursor = cursor;
                } else {
                    cursor++;
                }
            }

            if (lastCursor < cursor) {
                int number = Integer.parseInt(data.substring(lastCursor, cursor));
                current.segments.add(new Segment(number));
            }

            return current;
        }

        public Segment get(int index) {
            return index < segments.size() ? segments.get(index) : null;
        }
    }

    public Day13() {
        File input = new File("inputs/day13/input.txt");
        Scanner pairs;
        try {
            pairs = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int totalSum = 0;
        int pair = 1;

        while (pairs.hasNext()) {
            Segment packet1 = Segment.parse(pairs.nextLine());
            Segment packet2 = Segment.parse(pairs.nextLine());

            System.out.printf("== PAIR %d ==\n", pair);
            System.out.printf("- Compare %s vs %s\n", packet1, packet2);

            Boolean result = compare(packet1, packet2, 1);
            if (null == result || result) {
                System.out.println("--> OK");
                totalSum += pair;
            } else {
                System.out.println("--> NOT");
            }
            System.out.println();

            if (pairs.hasNext()) pairs.nextLine();
            pair++;
        }

        System.out.printf("The sum of the indices already in the right order is %d\n", totalSum);
    }

    private Boolean compare(Segment packet1, Segment packet2, int depth) {
        int cursor = 0;
        while (true) {
            Segment value1 = packet1.get(cursor);
            Segment value2 = packet2.get(cursor);

            if (value1 == null && value2 == null) {
                return null;
            }
            if (value1 == null) {
                printPadding(depth);
                System.out.print("- Left side ran out of items, so inputs are in the right order\n");
                return true;
            }
            if (value2 == null) {
                printPadding(depth);
                System.out.print("- Right side ran out of items, so inputs are not in the right order\n");
                return false;
            }

            Boolean result = compareSegments(value1, value2, depth);
            if (result != null) {
                return result;
            }

            cursor++;
        }
    }
    private Boolean compareSegments(Segment value1, Segment value2, int depth) {
        printPadding(depth);
        System.out.printf("- Compare %s vs %s\n", value1, value2);

        if (null != value1.value && null != value2.value) {
            if (value1.value < value2.value) {
                printPadding(depth);
                System.out.print("  - Left side is smaller, so inputs are in the right order\n");
                return true;
            } else if (value1.value > value2.value) {
                printPadding(depth);
                System.out.print("  - Right side is smaller, so inputs are not in the right order\n");
                return false;
            }
        } else {
            if (null == value1.value && null == value2.value) {
                Boolean result = compare(value1, value2, depth+1);
                if (null != result) {
                    return result;
                }
            }

            if (null != value1.value) {
                printPadding(depth);
                value1 = new Segment(value1);
                System.out.printf("- Mixed types; convert left to %s and retry comparison\n", value1);

                return compareSegments(value1, value2, depth + 1);
            }
            if (null != value2.value) {
                printPadding(depth);
                value2 = new Segment(value2);
                System.out.printf("- Mixed types; convert right to %s and retry comparison\n", value2);

                return compareSegments(value1, value2, depth + 1);
            }
        }

        return null;
    }

    private static void printPadding(int depth) {
        System.out.print("  ");
        while (depth > 0) {
            System.out.print("  ");
            depth--;
        }
    }
}
