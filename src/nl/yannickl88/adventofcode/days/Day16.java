package nl.yannickl88.adventofcode.days;

import nl.yannickl88.adventofcode.AlwaysScanner;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {
    class Valve {
        final String name;
        final int flowRate;

        HashMap<String, Integer> distances = new HashMap<>();
        HashMap<String, Integer> edges = new HashMap<>();
        int degree = 0;

        public Valve(String name, int flowRate) {
            this.name = name;
            this.flowRate = flowRate;
        }
    }

    class Edge {
        final String from, to;
        final int weight;

        public Edge(String from, String to) {
            this(from, to, 1);
        }
        public Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "%s -> %s".formatted(from, to);
        }
    }

    public Day16() {
        AlwaysScanner scans = new AlwaysScanner(new File("inputs/day16/test.txt"));
        Pattern pattern = Pattern.compile("Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z, ]+)");

        HashMap<String, Valve> valves = new HashMap<>();
        HashSet<Edge> edges = new HashSet<>();
        HashMap<String, Boolean> onVales = new HashMap<>();
        HashMap<String, Integer> visits = new HashMap<>();

        while (scans.hasNext()) {
            Matcher matcher = pattern.matcher(scans.nextLine());
            matcher.matches();

            Valve valve = new Valve(
                    matcher.group(1),
                    Integer.parseInt(matcher.group(2))
            );

            valves.put(valve.name, valve);
            onVales.put(valve.name, false);
            visits.put(valve.name, 0);

            for (String e : matcher.group(3).split(", ")) {
                edges.add(new Edge(valve.name, e));
            }
        }

        // Create distance graph
        for (String from : valves.keySet()) {
            for (String to : valves.keySet()) {
                if (from.equals(to)) {
                    continue;
                }

                valves.get(from).distances.put(to, distance(from, to, edges));
            }
        }

        // Optimize edges
        List<String> queue = valves.values().stream()
                .filter(v -> v.flowRate == 0 && !v.name.equals("AA"))
                .map(v -> v.name)
                .collect(Collectors.toList());

        while (!queue.isEmpty()) {
            String node = queue.remove(0);
            List<Edge> connectedNodes = edges.stream().filter(e -> e.to.equals(node)).collect(Collectors.toList());

            // Find all nodes which pass through here
            for (Edge newFrom : connectedNodes) {
                for (Edge newTo : connectedNodes) {
                    if (newFrom == newTo) {
                        continue;
                    }

                    edges.add(new Edge(newFrom.from, newTo.from, newFrom.weight + newTo.weight));
                }
            }

            // Remove any edges related to node
            edges.removeIf(n -> n.from.equals(node) || n.to.equals(node));
        }

        // Store edges
        for (Edge e : edges) {
            valves.get(e.from).edges.put(e.to, e.weight);
            valves.get(e.from).degree++;
            valves.get(e.to).degree++;
        }

        visits.put("AA", 1);

        var result = check(1, "AA", valves, edges, visits, onVales, 0);
        System.out.println(result);
    }

    private int check(
            int minutes,
            String currentPosition,
            HashMap<String, Valve> valves,
            HashSet<Edge> edges,
            HashMap<String, Integer> visits,
            HashMap<String, Boolean> onVales,
            int totalPressure
    ) {
        if (minutes > 30) {
            return totalPressure;
        }

        int pressure = currentPressure(valves, onVales);
        totalPressure += pressure;

        return Math.max(
                checkMoves(minutes, currentPosition, valves, edges, visits, onVales, totalPressure),
                checkOpen(minutes, currentPosition, valves, edges, visits, onVales, totalPressure)
        );
    }

    int currentPressure(HashMap<String, Valve> valves, HashMap<String, Boolean> onVales) {
        int total = 0;

        for (Valve v : valves.values()) {
            if (onVales.get(v.name)) {
                total += v.flowRate;
            }
        }

        return total;
    }

    private int checkOpen(
            int minutes,
            String currentPosition,
            HashMap<String, Valve> valves,
            HashSet<Edge> edges,
            HashMap<String, Integer> visits,
            HashMap<String, Boolean> onVales,
            int totalPressure
    ) {
        if (onVales.get(currentPosition) || valves.get(currentPosition).flowRate == 0) {
            return totalPressure;
        }

        HashMap<String, Boolean> newOnVales = new HashMap<>(onVales);
        newOnVales.put(currentPosition, true);

        return check(minutes + 1, currentPosition, valves, edges, visits, newOnVales, totalPressure);
    }

    private int checkMoves(
            int minutes,
            String currentPosition,
            HashMap<String, Valve> valves,
            HashSet<Edge> edges,
            HashMap<String, Integer> visits,
            HashMap<String, Boolean> onVales,
            int totalPressure
    ) {
        HashSet<String> selectedTargets = new HashSet<>();
        Valve current = valves.get(currentPosition);

        for (Valve valve : valves.values()) {
            if (onVales.get(valve.name) || valve.name.equals(currentPosition)) {
                continue;
            }
            int distance = current.distances.get(valve.name);

            if (minutes + distance > 30) {
                continue;
            }

            selectedTargets.add(valve.name);
        }

        if (selectedTargets.isEmpty()) {
            return totalPressure;
        }

        HashSet<String> selectedSteps = new HashSet<>();

        for (String v : selectedTargets) {
            // Find the neighbour which the smallest distance to that node.
            int smallestDist = Integer.MAX_VALUE;
            String node = null;

            for (String n : current.edges.keySet()) {
                if (visits.get(n) == valves.get(n).edges.size()) {
                    continue;
                }

                int distance = valves.get(n).distances.getOrDefault(v, 0);
                if (distance < smallestDist) {
                    smallestDist = distance;
                    node = n;
                }
            }

            if (node == null) {
                continue;
            }

            if (minutes + current.edges.get(node) > 30) {
                continue;
            }

            if (visits.get(node) == valves.get(node).degree) {
                continue;
            }

            selectedSteps.add(node);
        }

        if (selectedSteps.isEmpty()) {
            return totalPressure;
        }

        int bestTotalPressure = totalPressure;

        for (String node : selectedSteps) {
            HashMap<String, Integer> newVisits = new HashMap<>(visits);
            newVisits.put(node, newVisits.get(node) + 1);

            int newPressure = check(minutes + current.edges.get(node), node, valves, edges, newVisits, onVales, totalPressure);

            if (newPressure > bestTotalPressure) {
                bestTotalPressure = newPressure;
            }
        }

        return bestTotalPressure;
    }

    private int distance(String from, String to, HashSet<Edge> edges) {
        Map<String, Integer> distances = new HashMap<>();
        ArrayList<String> queue = new ArrayList<>();
        HashMap<String, String> previous = new HashMap<>();

        queue.add(from);
        distances.put(from, 0);

        while (!queue.isEmpty()) {
            queue.sort(Comparator.comparingInt(a -> distances.getOrDefault(a, Integer.MAX_VALUE)));
            String u = queue.remove(0);
            if (u.equals(to)) {
                break;
            }

            for (Edge e : edges) {
                if (!e.from.equals(u)) {
                    continue;
                }

                String v = e.to;

                int alt = distances.get(u) + 1;
                if (alt < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                    distances.put(v, alt);
                    previous.put(v, u);
                    queue.add(v);
                }
            }
        }

        String current = to;
        int distance = 0;

        while (current != null && previous.containsKey(current)) {
            distance++;
            current = previous.get(current);
        }

        return distance;
    }
}
