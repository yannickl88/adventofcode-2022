package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day1  {
    public Day1(File input) throws FileNotFoundException {
        int currentElf = 1;
        int currentCaloricCount = 0;
        int highestCaloricCarryingElf = -1;
        int highestCaloricCount = 0;

        Scanner elfs = new Scanner(input);

        while(elfs.hasNextLine()) {
            String line = elfs.nextLine();

            if (line.trim().length() == 0) {
                if (currentCaloricCount > highestCaloricCount) {
                    highestCaloricCarryingElf = currentElf;
                    highestCaloricCount = currentCaloricCount;
                }

                System.out.printf("Elf %d carries %d callories\n", currentElf, currentCaloricCount);

                currentElf++;
                currentCaloricCount = 0;
            } else {
                currentCaloricCount += Integer.parseInt(line);
            }
        }

        if (currentCaloricCount > highestCaloricCount) {
            highestCaloricCarryingElf = currentElf;
            highestCaloricCount = currentCaloricCount;
        }

        System.out.printf("Elf %d carries %d callories\n", currentElf, currentCaloricCount);
        System.out.println("------------");
        System.out.printf("Elf %d is carrying the most carries, which is %d callories\n", highestCaloricCarryingElf, highestCaloricCount);
    }
}
