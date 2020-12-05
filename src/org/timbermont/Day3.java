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

            // move to the right
            currentPosition += movement;
            currentPosition %= currentTopography.length();
        }
        System.out.println("Trees encountered: " + treesEncountered);

        System.out.println("Part 2:");
        int[][] slopes = {{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};
        long treesHitProduct = 1;

        for (int[] slope : slopes) {
            final int right = slope[0];
            final int down = slope[1];
            System.out.println(String.format("Checking slope [Right %d, down %d]", right, down));

            currentPosition = 0;
            treesEncountered = 0;

            for (int height = 0; height < topography.size(); height += down) {
                final String currentTopography = topography.get(height);

                if (TREE == currentTopography.charAt(currentPosition)) {
                    treesEncountered++;
                }

                // move to the right
                currentPosition += right;
                currentPosition %= currentTopography.length();
            }

            System.out.println("Trees encoutnered: " + treesEncountered);

            treesHitProduct *= treesEncountered;
        }

        System.out.println("Product of hit trees: " + treesHitProduct);
    }

    public static void loadInput() throws URISyntaxException, IOException {
        final Stream<String> lines = Files.lines(Path.of(Day1.class.getResource("Day3_input.txt").toURI()));
        topography = lines.collect(Collectors.toList());
    }
}
