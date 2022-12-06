package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day6 {
    public Day6() {
        File input = new File("inputs/day6/input.txt");
        Scanner stream;
        try {
            stream = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        stream.useDelimiter("");

        String window = ""; // padding at the front
        int charsScanned = 0;

        while (stream.hasNext()) {
            String c = stream.next();
            charsScanned++;

            window = window.substring(Math.max(0, window.length() - 3)) + c;

            if (window.length() ==4 && !containsDuplicateChars(window)) {
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
