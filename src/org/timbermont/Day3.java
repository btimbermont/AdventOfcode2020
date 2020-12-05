package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 {
    private static final char OPEN = '.';
    private static final char TREE = '#';

    private static List<String> topography;

    public static void main(final String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        final int movement = 3;
        int currentPosition = 0;
        int treesEncountered = 0;
        for (String currentTopography : topography) {
            if (TREE == currentTopography.charAt(currentPosition)) {
                treesEncountered++;
            }

            currentPosition += movement;
            currentPosition %= currentTopography.length();
        }
        System.out.println("Trees encoutnered: " + treesEncountered);
    }

    public static void loadInput() throws URISyntaxException, IOException {
        final Stream<String> lines = Files.lines(Path.of(Day1.class.getResource("Day3_input.txt").toURI()));
        topography = lines.collect(Collectors.toList());
    }
}
