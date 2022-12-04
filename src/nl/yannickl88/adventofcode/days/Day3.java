package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day3 {
    private static final String PRIORITY_ORDER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public Day3() {
        File input = new File("inputs/day3/input.txt");
        Scanner backpacks;
        try {
            backpacks = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int totalScore = 0;

        while (backpacks.hasNextLine()) {
            String backpackContents = backpacks.nextLine();
            String contentsFirstCompartment = backpackContents.substring(0, backpackContents.length() / 2);
            String contentsSecondCompartment = backpackContents.substring(backpackContents.length() / 2);

            String commonItem = this.findCommonItem(contentsFirstCompartment, contentsSecondCompartment);
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

    private String findCommonItem(String contentsFirstCompartment, String contentsSecondCompartment) {
        HashSet<String> itemsFirstCompartment = new HashSet<>(List.of(contentsFirstCompartment.split("")));
        HashSet<String> itemsSecondCompartment = new HashSet<>(List.of(contentsSecondCompartment.split("")));

        Set<String> intersection = new HashSet<String>(itemsFirstCompartment);
        intersection.retainAll(itemsSecondCompartment);

        return intersection.stream().findFirst().get();
    }
}
