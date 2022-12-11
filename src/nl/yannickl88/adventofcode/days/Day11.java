package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11 {
    static class Item {
        private static final int modulo = 9699690;
        private long value;

        Item(int number) {
            value = number;
        }

        void add(long value) {
            this.value = this.value % modulo + value % modulo;
        }

        void multiply(long value) {
            this.value = this.value % modulo * value % modulo;
        }

        void square() {
            this.multiply(this.value);
        }

        boolean isDivisibleBy(int value) {
            return this.value % value == 0;
        }
    }

    static class Monkey {
        private int name;
        private long inspections = 0;
        private final ArrayList<Item> items = new ArrayList<>();
        private final Consumer<Item> operation;
        private final Function<Item, Monkey> test;

        public Monkey(int name, List<Item> starting, Consumer<Item> operation, Function<Item, Monkey> test) {
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
                this.operation.accept(item);
                this.test.apply(item).items.add(item);
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
            Consumer<Item> operation = parseOperations(configuration.nextLine().substring(19));
            Function<Item, Monkey> test = parseCondition(
                    Integer.parseInt(configuration.nextLine().substring(21)),
                    Integer.parseInt(configuration.nextLine().substring(29)),
                    Integer.parseInt(configuration.nextLine().substring(30)),
                    monkeys
            );

            monkeys.add(new Monkey(name, starting, operation, test));

            if (configuration.hasNext())
                configuration.nextLine(); // empty line
        }

        for (int i = 0; i < 10000; i++) {
            for (Monkey monkey : monkeys) {
                monkey.play(false);
            }

            System.out.printf("== After round %d ==\n", i + 1);
            for (Monkey monkey : monkeys) {
                System.out.printf("Monkey %d inspected items %d times.\n", monkey.name, monkey.inspections);
            }
            System.out.println();
        }


        PriorityQueue<Monkey> monkeyInspections = new PriorityQueue<>((o1, o2) -> Long.compare(o2.inspections, o1.inspections));
        monkeyInspections.addAll(monkeys);

        long monkeyBusiness = Objects.requireNonNull(monkeyInspections.poll()).inspections * Objects.requireNonNull(monkeyInspections.poll()).inspections;

        System.out.printf("the level of monkey business is %d after 20 rounds of stuff-slinging simian shenanigans\n", monkeyBusiness);
    }

    private Consumer<Item> parseOperations(String operation) {
        if (operation.matches("^old \\* old$")) {
            return item -> item.square();
        }
        if (operation.matches("^old \\* [0-9]+$")) {
            int constant = Integer.parseInt(operation.substring(6));
            return item -> item.multiply(constant);
        }
        if (operation.matches("^old \\+ [0-9]+$")) {
            int constant = Integer.parseInt(operation.substring(6));
            return item -> item.add(constant);
        }
        throw new RuntimeException("Unknown operation " + operation);
    }

    private Function<Item, Monkey> parseCondition(int test, int ifTrue, int ifFalse, ArrayList<Monkey> monkeys) {
        return (Item item) -> monkeys.get(item.isDivisibleBy(test) ? ifTrue : ifFalse);
    }
}
