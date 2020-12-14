package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

public class Day9 {
    public static List<Long> input;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        int firstInvalidNumberIndex = findFirstInvalidNumberIndex(input, 25);
        System.out.println("first invalid index: " + (firstInvalidNumberIndex + 1) + " value: " + input.get(firstInvalidNumberIndex));

        System.out.println("Part 2:");
        final long desiredSum = input.get(firstInvalidNumberIndex);
        final List<Long> contiuousSet = findSetThatSums(input, desiredSum);
        System.out.println(contiuousSet + " sums up to " + desiredSum);
        // get largest and smallest, add together
        final LongSummaryStatistics summary = contiuousSet.stream().mapToLong(l -> l).summaryStatistics();
        System.out.println("weakness: " + (summary.getMax() + summary.getMin()));
    }

    private static List<Long> findSetThatSums(final List<Long> input, final long desiredSum) {
        int start = 0;
        int end = 1;

        while (end < input.size()) {
            long currentSum = sumSublist(input, start, end);
            if (currentSum == desiredSum) {
                return input.subList(start, end + 1);
            } else if (currentSum < desiredSum) {
                end++;
            } else {
                start++;
                if (start == end) {
                    end++;
                }
            }
        }
        return Collections.emptyList();
    }

    private static long sumSublist(final List<Long> list, final int from, final int to) {
        return list.subList(from, to + 1).stream().mapToLong(l -> l).sum();
    }

    public static int findFirstInvalidNumberIndex(final List<Long> list, final int preambleLength) {
        for (int i = preambleLength; i < list.size(); i++) {
            final List<Long> previousNumbers = list.subList(i - preambleLength, i);
            if (!sumCanBeMade(previousNumbers, list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean sumCanBeMade(final List<Long> candidates, final long sum) {
        final List<Long> sortedCandidates = new ArrayList<>(candidates);
        Collections.sort(sortedCandidates);
        for (Long value : sortedCandidates) {
            final long wantedValue = sum - value;
            if (wantedValue == value && Collections.frequency(sortedCandidates, value) > 1) {
                return true;
            } else if (wantedValue != value && Collections.binarySearch(sortedCandidates, wantedValue) >= 0) {
                return true;
            }
        }
        return false;
    }


    public static void loadInput() throws URISyntaxException, IOException {
        input = Files.lines(Path.of(Day9.class.getResource("Day9_input.txt").toURI()))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
