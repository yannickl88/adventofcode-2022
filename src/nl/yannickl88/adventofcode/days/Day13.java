package nl.yannickl88.adventofcode.days;

import nl.yannickl88.adventofcode.AlwaysScanner;

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
        AlwaysScanner pairs = new AlwaysScanner(new File("inputs/day13/input.txt"));

        ArrayList<Segment> packages = new ArrayList<>();
        Segment divider1 = Segment.parse("[[2]]");
        Segment divider2 = Segment.parse("[[6]]");

        packages.add(divider1);
        packages.add(divider2);

        while (pairs.hasNext()) {
            packages.add(Segment.parse(pairs.nextLine()));
            packages.add(Segment.parse(pairs.nextLine()));

            if (pairs.hasNext()) pairs.nextLine();
        }

        packages.sort((o1, o2) -> {
            Boolean result = compare(o1, o2);

            if (result == null) return 0;
            if (result) return -1;
            return 1;
        });

        for (Segment s : packages) {
            System.out.println(s);
        }

        System.out.println();
        System.out.printf("Indexes are %d and %d, decoder key is %d \n", packages.indexOf(divider1) + 1, packages.indexOf(divider2) + 1, (packages.indexOf(divider1) + 1) * (packages.indexOf(divider2) + 1));
    }

    private Boolean compare(Segment packet1, Segment packet2) {
        int cursor = 0;
        while (true) {
            Segment value1 = packet1.get(cursor);
            Segment value2 = packet2.get(cursor);

            if (value1 == null && value2 == null) {
                return null;
            }
            if (value1 == null) {
                return true;
            }
            if (value2 == null) {
                return false;
            }

            Boolean result = compareSegments(value1, value2);
            if (result != null) {
                return result;
            }

            cursor++;
        }
    }
    private Boolean compareSegments(Segment value1, Segment value2) {
        if (null != value1.value && null != value2.value) {
            if (value1.value < value2.value) {
                return true;
            } else if (value1.value > value2.value) {
                return false;
            }
        } else {
            if (null == value1.value && null == value2.value) {
                Boolean result = compare(value1, value2);
                if (null != result) {
                    return result;
                }
            }

            if (null != value1.value) {
                value1 = new Segment(value1);
                return compareSegments(value1, value2);
            }
            if (null != value2.value) {
                value2 = new Segment(value2);
                return compareSegments(value1, value2);
            }
        }

        return null;
    }
}
