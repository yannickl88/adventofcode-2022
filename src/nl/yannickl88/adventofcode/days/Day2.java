package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day2 {
    private enum OpponentChoice {
        ROCK("A"),
        PAPER("B"),
        SCISSORS("C");

        public final String choice;

        OpponentChoice(String choice) {
            this.choice = choice;
        }

        public static OpponentChoice fromString(String text) {
            for (OpponentChoice c : OpponentChoice.values()) {
                if (c.choice.equals(text)) {
                    return c;
                }
            }
            throw new RuntimeException();
        }
    }
    private enum PlayerChoice {
        ROCK("X", 1),
        PAPER("Y", 2),
        SCISSORS("Z", 3);

        public final String choice;
        public final int value;

        PlayerChoice(String choice, int value) {
            this.choice = choice;
            this.value = value;
        }

        public static PlayerChoice fromString(String text) {
            for (PlayerChoice c : PlayerChoice.values()) {
                if (c.choice.equals(text)) {
                    return c;
                }
            }
            throw new RuntimeException();
        }
    }

    public Day2() {
        File input = new File("inputs/day2/input.txt");
        Scanner rounds;
        try {
            rounds = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int round = 1;
        int totalScore = 0;

        while (rounds.hasNextLine()) {
            String[] line = rounds.nextLine().split(" ");
            OpponentChoice opponentChoice = OpponentChoice.fromString(line[0]);
            PlayerChoice playerChoice = PlayerChoice.fromString(line[1]);

            int score = playerChoice.value + this.score(opponentChoice, playerChoice);

            System.out.printf("round %d, opponent selected %s, you selected %s; score was %d\n", round, opponentChoice.name(), playerChoice.name(), score);

            round++;
            totalScore += score;
        }

        System.out.println("---------");
        System.out.printf("Your total score was %d\n", totalScore);
    }

    private int score(OpponentChoice opponentChoice, PlayerChoice playerChoice) {
        if (opponentChoice == OpponentChoice.ROCK) {
            if (playerChoice == PlayerChoice.ROCK) {
                return 3; // Draw
            }
            if (playerChoice == PlayerChoice.PAPER) {
                return 6; // Win
            }
        }
        if (opponentChoice == OpponentChoice.PAPER) {
            if (playerChoice == PlayerChoice.PAPER) {
                return 3; // Draw
            }
            if (playerChoice == PlayerChoice.SCISSORS) {
                return 6; // Win
            }

        }
        if (opponentChoice == OpponentChoice.SCISSORS) {
            if (playerChoice == PlayerChoice.SCISSORS) {
                return 3; // Draw
            }
            if (playerChoice == PlayerChoice.ROCK) {
                return 6; // Win
            }
        }
        return 0; // Loose
    }
}
