package org.timbermont;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {

    private static List<Integer> adapters;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        countJoltJumps();

        System.out.println("Part 2:");
        countWaysToConnect();
    }

    private static void countJoltJumps() {
        final List<Integer> sortedAdapters = new ArrayList<>(adapters);
        Collections.sort(sortedAdapters);

        int currentJolt = 0;
        int[] joltJumps = new int[4];

        for (Integer currentAdapter : sortedAdapters) {
            final int difference = currentAdapter - currentJolt;
            if (difference > 3) {
                throw new IllegalStateException(String.format("cannot jump from %d to %d, adapters: %s",
                        currentJolt,
                        currentAdapter,
                        sortedAdapters));
            }
            joltJumps[difference]++;
            currentJolt = currentAdapter;
        }

        // own device is always 3 higher than last adapter
        currentJolt += 3;
        joltJumps[3]++;

        System.out.println(String.format("jolt jumps: %s, final joltage: %d", Arrays.toString(joltJumps), currentJolt));
        System.out.println("Product of jolt jumps 1 and 3: " + joltJumps[1] * joltJumps[3]);
    }

    private static void countWaysToConnect() {
        // All initial differences between two consecutive adapters are either 1 or 3 jolts.
        // So, all variations stem from the 1-jolt jumps.
        // So, we count the length of all these '1 jolt chains', and calculate the variations
        // of these chains themselves. then, we multiply all these variations to get the total number of
        // possible variations.

        final ArrayList<Integer> sortedAdapters = new ArrayList<>(adapters);
        Collections.sort(sortedAdapters);

        // construct list of 1 jump chains
        final List<Integer> singleJoltChains = new ArrayList<>();
        int currentStreak = 0;
        int lastAdapter = 0; // outlet is 0
        for (int currentAdapter : sortedAdapters) {
            if (currentAdapter - lastAdapter == 1) {
                currentStreak++;
            } else {
                if (currentStreak > 0) singleJoltChains.add(currentStreak);
                currentStreak = 0;
            }
            lastAdapter = currentAdapter;
        }
        if (currentStreak != 0) singleJoltChains.add(currentStreak);

        System.out.println("Single jolt chain lengths: " + singleJoltChains);

        // calculate the possible variations for each single-jolt-chain
        // then multiply the chains with each other for all possible variations
        BigInteger total = BigInteger.ONE;
        for (int singleJoltChain : singleJoltChains) {
            long variations = variationsForSingleJoltChain(singleJoltChain);
            total = total.multiply(BigInteger.valueOf(variations));
        }
        System.out.println("total amount of variations: " + total);
    }

    /**
     * Calculates the amount of variations of valid adapter arrangements possible for chains of x single jolt jumps.
     * A chain of length n corresponds with a chain of n+1 adapters of which the voltage raises by 1 per adapter.
     * A valid adapter arrangement means:
     *    - keeping the first and last adapter in place
     *    - not making a jump higher than 3, aka don't take out more than 2 consecutive adapters from the full chain.
     * We can count this using induction using these base cases:
     *     1. a chain of 1 (2 adapters) has only 1 variation (keep both)
     *     2. a chain of 2 (3 adapters) has 2 variations (either keep or take out the middle one)
     *     3. a chain of 3 (4 adapters) has 4 variations (keep all/take out adapter 2/take out 3/take out 2 and 3)
     * All other cases with x adapters:
     *     keep the first adapter (we have to), and sum
     *         - count the variations of x-1 adapters
     *         - count the variations of x-2 adapters (aka: take the second out)
     *         - count the variations of x-3 adapters (aka: take second and third adapter out)
     */
    private static long variationsForSingleJoltChain(final int chainLength) {
        if (chainLength == 1) return 1;
        if (chainLength == 2) return 2;
        if (chainLength == 3) return 4;
        return variationsForSingleJoltChain(chainLength - 1)
                + variationsForSingleJoltChain(chainLength - 2)
                + variationsForSingleJoltChain(chainLength - 3);

    }

    public static void loadInput() throws URISyntaxException, IOException {
        adapters = Files.lines(Path.of(Day10.class.getResource("Day10_input.txt").toURI()))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
