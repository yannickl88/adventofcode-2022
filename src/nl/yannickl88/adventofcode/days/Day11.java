package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11 {
    static class Item {
        long worryLevel;

        public Item(int level) {
            worryLevel = level;
        }
    }

    static class Monkey {
        private int name;
        private int inspections = 0;
        private final ArrayList<Item> items = new ArrayList<>();
        private final Function<Long, Long> operation;
        private final Function<Long, Monkey> test;

        public Monkey(int name, List<Item> starting, Function<Long, Long> operation, Function<Long, Monkey> test) {
            this.name = name;
            this.operation = operation;
            this.test = test;
            items.addAll(starting);
        }

        public void play(boolean verbose) {
            if (verbose) System.out.printf("Monkey %d:\n", name);

            while (this.items.size() > 0) {
                Item item = items.remove(0);

                // Inspects
                inspections++;
                if (verbose) System.out.printf("  Monkey inspects an item with a worry level of %d.\n", item.worryLevel);

                // Apply operation
                item.worryLevel = this.operation.apply(item.worryLevel);
                if (verbose) System.out.printf("    Worry level is updated to %d.\n", item.worryLevel);

                // Boredom
                item.worryLevel = item.worryLevel / 3;
                if (verbose) System.out.printf("    Monkey gets bored with item. Worry level is divided by 3 to %d.\n", item.worryLevel);

                // Select
                Monkey target = this.test.apply(item.worryLevel);
                target.items.add(item);
                if (verbose) System.out.printf("    Item with worry level %d is thrown to monkey %d.\n", item.worryLevel, target.name);
            }
        }
    }

    public Day11() {
        File input = new File("inputs/day11/input.txt");
        Scanner configuration;
        try {
            configuration = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Monkey> monkeys = new ArrayList<>();

        while (configuration.hasNext()) {
            int name = Integer.parseInt(configuration.nextLine().substring(7, 8));
            List<Item> starting = Arrays.stream(
                    configuration
                            .nextLine()
                            .substring(18)
                            .split(",")
                    )
                    .map(s -> new Item(Integer.parseInt(s.trim())))
                    .collect(Collectors.toList());
            Function<Long, Long> operation = parseOperations(configuration.nextLine().substring(19));
            Function<Long, Monkey> test = parseCondition(
                    Integer.parseInt(configuration.nextLine().substring(21)),
                    Integer.parseInt(configuration.nextLine().substring(29)),
                    Integer.parseInt(configuration.nextLine().substring(30)),
                    monkeys
            );

            monkeys.add(new Monkey(name, starting, operation, test));

            if (configuration.hasNext())
                configuration.nextLine(); // empty line
        }

        for (int i = 0; i < 20; i++) {
            for (Monkey monkey : monkeys) {
                monkey.play(true);
            }

            System.out.printf("After round %d, the monkeys are holding items with these worry levels:\n", i + 1);
            for (Monkey monkey : monkeys) {
                System.out.printf("  Monkey %d: %s\n", monkey.name, monkey.items.stream().map(item -> String.valueOf(item.worryLevel)).collect(Collectors.joining(", ")));
            }
        }

        System.out.println();

        for (Monkey monkey : monkeys) {
            System.out.printf("Monkey %d inspected items %d times.\n", monkey.name, monkey.inspections);
        }

        PriorityQueue<Monkey> monkeyInspections = new PriorityQueue<>((o1, o2) -> Integer.compare(o2.inspections, o1.inspections));
        monkeyInspections.addAll(monkeys);

        int monkeyBusiness = Objects.requireNonNull(monkeyInspections.poll()).inspections * Objects.requireNonNull(monkeyInspections.poll()).inspections;

        System.out.printf("the level of monkey business is %d after 20 rounds of stuff-slinging simian shenanigans\n", monkeyBusiness);
    }

    private Function<Long, Long> parseOperations(String operation) {
        if (operation.matches("^old \\* old$")) {
            return (Long old) -> old * old;
        }
        if (operation.matches("^old \\* [0-9]+$")) {
            long constant = Integer.parseInt(operation.substring(6));
            return (Long old) -> old * constant;
        }
        if (operation.matches("^old \\+ [0-9]+$")) {
            long constant = Integer.parseInt(operation.substring(6));
            return (Long old) -> old + constant;
        }
        throw new RuntimeException("Unknown operation " + operation);
    }

    private Function<Long, Monkey> parseCondition(long test, int ifTrue, int ifFalse, ArrayList<Monkey> monkeys) {
        return (Long old) -> monkeys.get(old % test == 0 ? ifTrue : ifFalse);
    }
}
