package nl.yannickl88.adventofcode.days;

import nl.yannickl88.adventofcode.AlwaysScanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Day1 {
    private static final int TOTAL = 3;
    static class Elf {
        public final int index;
        public final int calories;

        Elf(int index, int calories) {

            this.index = index;
            this.calories = calories;
        }
    }

    public Day1() {
        AlwaysScanner elfs = new AlwaysScanner(new File("inputs/day1/input.txt"));

        PriorityQueue<Elf> queue = new PriorityQueue<>((o1, o2) -> Integer.compare(o2.calories, o1.calories));
        int currentElf = 1;
        int currentCaloricCount = 0;

        while (elfs.hasNextLine()) {
            String line = elfs.nextLine();

            if (line.trim().length() == 0) {
                queue.add(new Elf(currentElf, currentCaloricCount));

                currentElf++;
                currentCaloricCount = 0;
            } else {
                currentCaloricCount += Integer.parseInt(line);
            }
        }

        queue.add(new Elf(currentElf, currentCaloricCount));

        int totalByTop3 = 0;

        for (int i = 0; i < TOTAL; i++) {
            Elf e = queue.remove();
            System.out.printf("Elf %d is carrying the most carries, which is %d callories\n", e.index, e.calories);

            totalByTop3 += e.calories;
        }

        System.out.println("---------");
        System.out.printf("Total by all 3 is %d callories\n", totalByTop3);

    }
}
