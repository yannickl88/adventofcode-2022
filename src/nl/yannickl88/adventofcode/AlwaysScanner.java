package nl.yannickl88.adventofcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AlwaysScanner {
    private final Scanner scanner;

    public AlwaysScanner(File file) {
        try {
            this.scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public void useDelimiter(String s) {
        scanner.useDelimiter(s);
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public String next() {
        return scanner.next();
    }

    public boolean hasNext(Pattern pattern) {
        return scanner.hasNext(pattern);
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }
}
