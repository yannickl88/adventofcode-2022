package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 {
    public Day5() {
        File input = new File("inputs/day5/input.txt");
        Scanner instructions;
        try {
            instructions = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ArrayList<ArrayList<String>> crates = new ArrayList<>();
        boolean readingInstructions = false; // true, once the crate configuration is done
        Pattern pattern = Pattern.compile("^move ([0-9]+) from ([0-9]+) to ([0-9]+)$");

        while (instructions.hasNextLine()) {
            String line = instructions.nextLine();

            if (line.trim().length() == 0) {
                readingInstructions = true;
                continue;
            }

            if (!readingInstructions) {
                if (line.trim().charAt(0) != '[') {
                    continue; // Crate label line
                }

                // Reading create configuration
                int pos = 1;
                int index = 0;
                while (pos < line.length()) {
                    String item = String.valueOf(line.charAt(pos));

                    if (!item.equals(" ")) {
                        while (crates.size() <= index) {
                            crates.add(new ArrayList<>());
                        }
                        ArrayList<String> crate = crates.get(index);
                        crate.add(0, item);
                    }
                    pos += 4;
                    index++;
                }
            } else {
                // Reading instructions
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int amount = Integer.parseInt(matcher.group(1));
                    int from = Integer.parseInt(matcher.group(2));
                    int to = Integer.parseInt(matcher.group(3));

                    ArrayList<String> fromCrate = crates.get(from - 1);
                    ArrayList<String> toCrate = crates.get(to - 1);

                    while (amount > 0) {
                        toCrate.add(fromCrate.remove(fromCrate.size() - 1));

                        amount--;
                    }
                }
            }
        }

        StringBuilder result = new StringBuilder();

        for (ArrayList<String> crate : crates) {
            result.append(crate.get(crate.size() - 1));
        }

        System.out.printf("Top os each stack is: %s\n", result);
    }
}
