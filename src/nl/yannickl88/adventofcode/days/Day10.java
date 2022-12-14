package nl.yannickl88.adventofcode.days;

import nl.yannickl88.adventofcode.AlwaysScanner;

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
            if (cycles % 40 == 0) {
                System.out.println();
            }
            System.out.printf(overlaps(this.registerValue, cycles % 40) ? "#" : ".");

            cycles++;
            this.registerValue = registerValue;
        }

        private boolean overlaps(int registerValue, int position) {
            return position >= registerValue - 1 && position <= registerValue + 1;
        }
    }

    public Day10() {
        AlwaysScanner instructions = new AlwaysScanner(new File("inputs/day10/input.txt"));
        CPU cpu = new CPU();

        while (instructions.hasNext()) {
            String[] instruction = instructions.nextLine().split(" ", 2);

            if (instruction[0].equals("noop")) {
                cpu.noop();
            } else if (instruction[0].equals("addx")) {
                cpu.addx(Integer.parseInt(instruction[1]));
            }
        }

        System.out.println();
    }
}
