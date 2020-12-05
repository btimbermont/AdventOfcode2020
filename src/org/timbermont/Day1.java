package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day1 {

    private static Collection<Integer> input;
    private static final int desiredSum = 2020;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("part 1:");
        for (Integer candidate : input) {
            int desiredOtherElement = desiredSum - candidate;
            if (input.contains(desiredOtherElement)) {
                System.out.println(String.format("Found pair %d and %d, their product is %d.", candidate, desiredOtherElement, candidate * desiredOtherElement));
                break;
            }
        }

        System.out.println("part 2:");
        OUTER:
        for (int c1 : input) {
            for (int c2 : input) {
                if (c2 == c1) {
                    continue;
                }
                int desiredThirdElement = desiredSum - c1 - c2;
                if (input.contains(desiredThirdElement)) {
                    System.out.println(String.format("found 3 numebrs: %d, %d, %d, their product is %d",
                            c1, c2, desiredThirdElement, c1 * c2 * desiredThirdElement));
                    break OUTER;
                }
            }
        }
    }

    private static void loadInput() throws URISyntaxException, IOException {
        Stream<String> lines = Files.lines(Path.of(Day1.class.getResource("Day1_input.txt").toURI()));
        input = lines
                .map(Integer::valueOf)
                .collect(Collectors.toCollection(TreeSet::new));
        lines.close();
    }
}
