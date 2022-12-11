package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day11 {
    static class Item {
        private static final int[] factors = new int[]{
                2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1009, 1013, 1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063, 1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123, 1129, 1151, 1153, 1163, 1171, 1181, 1187, 1193, 1201, 1213, 1217, 1223, 1229, 1231, 1237, 1249, 1259, 1277, 1279, 1283, 1289, 1291, 1297, 1301, 1303, 1307, 1319, 1321, 1327, 1361, 1367, 1373, 1381, 1399, 1409, 1423, 1427, 1429, 1433, 1439, 1447, 1451, 1453, 1459, 1471, 1481, 1483, 1487, 1489, 1493, 1499, 1511, 1523, 1531, 1543, 1549, 1553, 1559, 1567, 1571, 1579, 1583, 1597, 1601, 1607, 1609, 1613, 1619, 1621, 1627, 1637, 1657, 1663, 1667, 1669, 1693, 1697, 1699, 1709, 1721, 1723, 1733, 1741, 1747, 1753, 1759, 1777, 1783, 1787, 1789, 1801, 1811, 1823, 1831, 1847, 1861, 1867, 1871, 1873, 1877, 1879, 1889, 1901, 1907, 1913, 1931, 1933, 1949, 1951, 1973, 1979, 1987, 1993, 1997, 1999
        };
        private int[] exponents;

        Item(int number) {
            exponents = factorize(number);
        }

        static int[] factorize(long number) {
            int[] exponents = new int[factors.length];

            while (number > 1) {
                boolean hasFactored = false;
                for (int i = 0; i < factors.length; i++) {
                    if (number % factors[i] == 0) {
                        number = number / factors[i];
                        exponents[i]++;

                        hasFactored = true;
                        break;
                    }
                }

                if (!hasFactored) {
                    throw new RuntimeException("Cannot factor number " + number);
                }
            }

            return exponents;
        }

        Item add(int value) {
            this.exponents = factorize(this.value() + (long) value);

            return this;
        }

        Item multiply(int value) {
            int[] exponents = factorize(value);

            for (int i = 0; i < factors.length; i++) {
                this.exponents[i] += exponents[i];
            }

            return this;
        }

        Item square() {
            for (int i = 0; i < factors.length; i++) {
                this.exponents[i] += exponents[i];
            }

            return this;
        }

        boolean isDivisibleBy(int value) {
            return false;
        }

        long value() {
            long total = 1;

            for (int i = 0; i < factors.length; i++) {
                total *= (long) Math.pow(factors[i], exponents[i]);
            }

            return total;
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
        File input = new File("inputs/day11/test.txt");
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
