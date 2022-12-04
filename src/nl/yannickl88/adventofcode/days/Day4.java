package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day4 {

    static class Section {
        public int start;
        public int end;

        Section(String range) {
            String[] parts = range.split("-");

            start = Integer.parseInt(parts[0]);
            end = Integer.parseInt(parts[1]);
        }

        public boolean fullyOverlaps(Section other) {
            return start <= other.start && end >= other.end;
        }

        public boolean partlyOverlaps(Section other) {
            return end >= other.start && start <= other.end;
        }

        public void print(int size) {
            StringBuilder row = new StringBuilder();

            for (int i = 1; i <= size; i++) {
                if (i >= start && i <= end) {
                    row.append(i);
                } else {
                    row.append(".");
                }
            }

            row.append("  ");
            row.append(start);
            row.append("-");
            row.append(end);

            System.out.println(row);
        }
    }

    public Day4() {
        File input = new File("inputs/day4/input.txt");
        Scanner pairs;
        try {
            pairs = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int overlappingSections = 0;

        while (pairs.hasNextLine()) {
            String[] pair = pairs.nextLine().split(",");

            Section firstSection = new Section(pair[0]);
            Section secondSection = new Section(pair[1]);

            if (firstSection.partlyOverlaps(secondSection) || secondSection.partlyOverlaps(firstSection)) {
                overlappingSections++;
            }
        }

        System.out.printf("%d pairs partly contain the other.\n", overlappingSections);
    }
}
