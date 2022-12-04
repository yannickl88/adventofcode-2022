package nl.yannickl88.adventofcode;

import nl.yannickl88.adventofcode.days.*;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new Day1(new File("inputs/day1/input.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
