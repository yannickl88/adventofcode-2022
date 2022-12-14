package nl.yannickl88.adventofcode.days;

import nl.yannickl88.adventofcode.AlwaysScanner;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day3 {
    private static final String PRIORITY_ORDER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public Day3() {
        AlwaysScanner backpacks = new AlwaysScanner(new File("inputs/day3/input.txt"));
        int totalScore = 0;

        while (backpacks.hasNextLine()) {
            String backpack1Contents = backpacks.nextLine();
            String backpack2Contents = backpacks.nextLine();
            String backpack3Contents = backpacks.nextLine();

            String commonItem = this.findCommonItem(backpack1Contents, backpack2Contents, backpack3Contents);
            int score = this.priority(commonItem);

            System.out.printf("Common item is %s, with a score of %d.\n", commonItem, score);

            totalScore += score;
        }

        System.out.println("----");
        System.out.printf("Total score is %d.\n", totalScore);
    }

    private int priority(String letter) {
        return PRIORITY_ORDER.indexOf(letter) + 1;
    }

    private String findCommonItem(String backpack1Contents, String backpack2Contents, String backpack3Contents) {
        HashSet<String> itemsBackpack1 = new HashSet<>(List.of(backpack1Contents.split("")));
        HashSet<String> itemsBackpack2 = new HashSet<>(List.of(backpack2Contents.split("")));
        HashSet<String> itemsBackpack3 = new HashSet<>(List.of(backpack3Contents.split("")));

        Set<String> intersection = new HashSet<>(itemsBackpack1);
        intersection.retainAll(itemsBackpack2);
        intersection.retainAll(itemsBackpack3);

        return intersection.stream().findFirst().get();
    }
}
