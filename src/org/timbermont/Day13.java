package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {

    private static int earliestMinute;
    private static List<Integer> busLines;
    private static List<BuslineRequirement> buslineRequirements;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();
        System.out.println(earliestMinute);
        System.out.println(busLines);

        System.out.println("Part 1:");
        int minWaitTime = Integer.MAX_VALUE, earliestLine = -1;
        for (Integer busLine : busLines) {
            int currentWaitTime = busLine - (earliestMinute % busLine);
            if (currentWaitTime < minWaitTime) {
                earliestLine = busLine;
                minWaitTime = currentWaitTime;
            }
        }
        System.out.println(String.format("Earliest busline is %d with a wait time of %d minutes", earliestLine, minWaitTime));
        System.out.println("Product: " + (earliestLine * minWaitTime));

        System.out.println("Part 2:");
        loadInput2();
        findSpecificMinute();
    }

    public static void loadInput() throws URISyntaxException, IOException {
        List<String> allLines = Files.readAllLines(Path.of(Day13.class.getResource("Day13_input.txt").toURI()));
        earliestMinute = Integer.parseInt(allLines.get(0));
        busLines = Arrays.stream(allLines.get(1).split(","))
                .filter(s -> !"x".equals(s))
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());
    }

    public static void loadInput2() throws URISyntaxException, IOException {
        buslineRequirements = new ArrayList<>();
        List<String> allLines = Files.readAllLines(Path.of(Day13.class.getResource("Day13_input.txt").toURI()));
        String[] entries = allLines.get(1).split(",");
        for (int i = 0; i < entries.length; i++) {
            if (!"x".equals(entries[i])) {
                buslineRequirements.add(new BuslineRequirement(Integer.parseInt(entries[i]), i));
            }
        }
    }

    private static void findSpecificMinute() {
        long currentMinute = 0;
        long currentBase = 1;

        for (BuslineRequirement requirement : buslineRequirements) {
            long i = 0;
            while (!checkRequirement(currentMinute, requirement)) {
                currentMinute += currentBase;
                i++;
                if (i % 1_000_000 == 0) System.out.println("Currently checking minute " + currentMinute);
            }
            currentBase = lcm(currentBase, requirement.busline);
            System.out.println(String.format("After applying %s, currentbase: %d, currentminute: %d", requirement, currentBase, currentMinute));
        }

        System.out.println();
    }

    private static boolean checkRequirement(final long minuteToCheck, final BuslineRequirement requirement) {
        final long expectedArrivalTime = minuteToCheck + requirement.delayAfterFirstBus;
        return expectedArrivalTime % requirement.busline == 0;
    }

    private static long lcm(final long a, final long b) {
        System.out.println("LCM of " + a + " and " + b);
        final long max = Math.max(a, b);
        long lcm = max;
        while (lcm % a != 0 || lcm % b != 0) {
            lcm += max;
        }
        System.out.println("lcm found: " + lcm);
        return lcm;
    }

    private static class BuslineRequirement {
        private final int busline;
        private final int delayAfterFirstBus;

        public BuslineRequirement(int busline, int delayAfterFirstBus) {
            this.busline = busline;
            this.delayAfterFirstBus = delayAfterFirstBus;
        }

        @Override
        public String toString() {
            return "BuslineRequirement{" +
                    "busline=" + busline +
                    ", delayAfterFirstBus=" + delayAfterFirstBus +
                    '}';
        }
    }

}
