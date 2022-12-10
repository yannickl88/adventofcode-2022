package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day10 {
    static class CPU {
        int cycles = 0;
        ArrayList<Integer> signalStrengths = new ArrayList<>();
        int registerValue = 1;

        void noop() {
            cycle(registerValue);
        }

        public void addx(int v) {
            cycle(registerValue);
            cycle(registerValue + v);
        }

        private void cycle(int registerValue) {
            cycles++;

            if ((cycles + 20) % 40 == 0) {
                System.out.printf("Cycle %d the registry value at the start is %d, after is %d\n", cycles, this.registerValue, registerValue);
                signalStrengths.add(this.registerValue * cycles);
            }
            this.registerValue = registerValue;
        }
    }

    public Day10() {
        File input = new File("inputs/day10/test2.txt");
        Scanner instructions;
        try {
            instructions = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        CPU cpu = new CPU();

        while (instructions.hasNext()) {
            String[] instruction = instructions.nextLine().split(" ", 2);

            if (instruction[0].equals("noop")) {
                cpu.noop();
            } else if (instruction[0].equals("addx")) {
                cpu.addx(Integer.parseInt(instruction[1]));
            }
        }

        System.out.println(cpu.signalStrengths);
        System.out.println(cpu.signalStrengths.stream().reduce(Integer::sum));
    }
}
