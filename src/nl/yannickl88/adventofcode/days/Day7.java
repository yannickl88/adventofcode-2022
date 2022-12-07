package nl.yannickl88.adventofcode.days;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Day7 {
    static class FileNode {
        public final boolean isFolder;

        public final String name;

        public int size;

        public FileNode parent;

        private final ArrayList<FileNode> children = new ArrayList<>();

        FileNode(String name, boolean isFolder) {
            this.name = name;
            this.isFolder = isFolder;
        }

        public void add(FileNode node) {
            this.children.add(node);

            node.parent = this;
        }

        public FileNode get(String loc) {
            for (FileNode n : this.children) {
                if (n.isFolder && n.name.equals(loc)) {
                    return n;
                }
            }

            throw new RuntimeException("Cannot find directory " + loc);
        }

        public int totalSize() {
            if (!this.isFolder) {
                return size;
            }

            int total = 0;

            for (FileNode n : this.children) {
                total += n.totalSize();
            }
            return total;
        }

        public void collectFolders(ArrayList<Integer> result, int totalSize) {
            if (!this.isFolder) {
                return;
            }

            int mySize = this.totalSize();

            if (mySize >= totalSize) {
                result.add(mySize);
            }

            for (FileNode n : this.children) {
                n.collectFolders(result, totalSize);
            }
        }
    }
    public Day7() {
        File input = new File("inputs/day7/input.txt");
        Scanner commands;
        try {
            commands = new Scanner(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Pattern pattern = Pattern.compile("\\$.*");

        FileNode root = new FileNode("/", true);
        FileNode cwd = root;

        while (commands.hasNext()) {
            String line = commands.nextLine();

            if (line.startsWith("$ cd /")) {
                // DO nothing
            } else if (line.startsWith("$ cd ..")) {
                cwd = cwd.parent;
            } else if (line.startsWith("$ cd")) {
                String loc = line.substring(5);

//                System.out.println(loc);
                cwd = cwd.get(loc);
            } else if (line.startsWith("$ ls")) {
                while (commands.hasNext() && !commands.hasNext(pattern)) {
                    String[] data = commands.nextLine().split(" ", 2);
                    FileNode node = new FileNode(data[1], data[0].equals("dir"));

                    if (!node.isFolder) {
                        node.size = Integer.parseInt(data[0]);
                    }

                    cwd.add(node);
                }
            } else {
                System.out.println("Unknown command, " + line);
            }
        }

        int totalSize = root.totalSize();

        ArrayList<Integer> result = new ArrayList<>();
        System.out.printf("Total size of all file is %d\n", totalSize);

        int toFreeUp = 30000000 - (70000000 - totalSize);
        System.out.printf("We need to remove a folder of at least %d to clear up enough space\n", toFreeUp);

        root.collectFolders(result, toFreeUp);

        System.out.println(result);

        int total = result.stream().reduce(root.totalSize(), Integer::min);

        System.out.printf("Smallest folder size we can remove is %d\n", total);
    }
}
