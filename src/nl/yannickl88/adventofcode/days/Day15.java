package nl.yannickl88.adventofcode.days;

import nl.yannickl88.adventofcode.AlwaysScanner;
import nl.yannickl88.adventofcode.util.Grid;
import nl.yannickl88.adventofcode.util.GridUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 {
    static enum Item {
        BEACON, SENSOR, NOTHING
    }

    static class Sensor {
        final int x, y, range;

        public Sensor(int x, int y, int range) {
            this.x = x;
            this.y = y;
            this.range = range;
        }

        public boolean inRange(int x, int y) {
            return Math.abs(this.x - x) + Math.abs(this.y - y) <= range;
        }
    }

    public Day15() {
        AlwaysScanner scans = new AlwaysScanner(new File("inputs/day15/input.txt"));
        Pattern pattern = Pattern.compile("Sensor at x=([-0-9]+), y=([-0-9]+): closest beacon is at x=([-0-9]+), y=([-0-9]+)");

        List<String> lines = scans.lines();
        ArrayList<Sensor> sensors = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            matcher.matches();

            int sensorX = Integer.parseInt(matcher.group(1)), sensorY = Integer.parseInt(matcher.group(2));
            int beaconX = Integer.parseInt(matcher.group(3)), beaconY = Integer.parseInt(matcher.group(4));
            int range = GridUtils.distanceManhattan(sensorX, sensorY, beaconX, beaconY);

            sensors.add(new Sensor(sensorX, sensorY, range));
        }

        for (Sensor s : sensors) {
            // get the points on the border
            for (int i = 0; i <= s.range; i++) {
                int x, y;
                // Top
                x = s.x + i;
                y = s.y - (s.range - i + 1);

                inInRangeOf(x, y, sensors);

                // Bottom
                x = s.x - i;
                y = s.y + (s.range - i + 1);

                inInRangeOf(x, y, sensors);

                // Left
                x = s.x - (s.range - i + 1);
                y = s.y - i;

                inInRangeOf(x, y, sensors);

                // Right
                x = s.x + (s.range - i + 1);
                y = s.y + i;

                inInRangeOf(x, y, sensors);
            }
        }

        System.out.println("HEY");
    }

    private void inInRangeOf(int x, int y, ArrayList<Sensor> sensors) {
        int max = 4000000;

        if (x < 0 || x > max || y < 0 || y > max) {
            return;
        }

        for (Sensor s : sensors) {
            if (GridUtils.distanceManhattan(x, y, s.x, s.y) <= s.range) {
                return;
            }
        }

        System.out.printf("tuning frequency of point (%d,%d) is %d\n", x, y, (long) x * 4000000 + y);
    }
}
