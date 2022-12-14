package nl.yannickl88.adventofcode.days;

import nl.yannickl88.adventofcode.AlwaysScanner;

import java.io.File;

public class Day6 {
    private static final int MESSAGE_SIZE = 14;

    public Day6() {
        AlwaysScanner stream = new AlwaysScanner(new File("inputs/day6/input.txt"));
        stream.useDelimiter("");

        String window = ""; // padding at the front
        int charsScanned = 0;

        while (stream.hasNext()) {
            String c = stream.next();
            charsScanned++;

            window = window.substring(Math.max(0, window.length() - (MESSAGE_SIZE - 1))) + c;

            if (window.length() == MESSAGE_SIZE && !containsDuplicateChars(window)) {
                System.out.printf("first marker after character %d\n", charsScanned);
                return;
            }
        }
    }

    boolean containsDuplicateChars(String sequence) {
        for (int i = 0, l = sequence.length(); i < l; i++) {
            for (int j = i + 1; j < l; j++) {
                if (sequence.charAt(i) == sequence.charAt(j)) {
                    return true;
                }
            }
        }

        return false;
    }
}
