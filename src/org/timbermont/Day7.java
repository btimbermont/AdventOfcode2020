package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7 {

    private static Map<String, BagRule> bagRules;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        System.out.println(howManyCanContain("shiny gold"));

        System.out.println("part 2:");
        System.out.println(howMayAreContainedIn("shiny gold"));
    }

    private static void loadInput() throws URISyntaxException, IOException {
        final Stream<String> lines = Files.lines(Path.of(Day7.class.getResource("Day7_input.txt").toURI()));

        // loadInput
        bagRules = lines.map(BagRule::new)
                .collect(Collectors.toMap(rule -> rule.color, Function.identity()));
        // process input: reverse relations
        for (BagRule rule : bagRules.values()) {
            for (String canContainThisColor : rule.containsOtherbags.keySet()) {
                bagRules.get(canContainThisColor).canBeContainedIn.add(rule.color);
            }
        }
    }

    public static int howManyCanContain(String color) {
        Set<String> canBeContainedIn = new HashSet<>(bagRules.get(color).canBeContainedIn);

        keepGrowingUntilStable(canBeContainedIn);

        return canBeContainedIn.size();
    }

    public static long howMayAreContainedIn(final String color) {
        long amountWithin = 0;
        final BagRule bagRule = bagRules.get(color);
        for (String colorWithin : bagRule.containsOtherbags.keySet()) {
            final int amount = bagRule.containsOtherbags.get(colorWithin);
            amountWithin += amount * (1 + howMayAreContainedIn(colorWithin));
        }
        return amountWithin;
    }

    public static void keepGrowingUntilStable(final Set<String> setToGrow) {
        while (true) {
            final int initialSize = setToGrow.size();

            setToGrow.addAll(setToGrow.stream().map(color -> bagRules.get(color))
                    .map(rule -> rule.canBeContainedIn)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet()));
            final int newSize = setToGrow.size();

            if (newSize == initialSize) break;
        }
    }

    private static class BagRule {
        private static final Pattern rulePattern = Pattern.compile("(.*) bags contain (.*)\\.");
        private static final Pattern containsPattern = Pattern.compile("(\\d+) (.*) bags?");

        private final String rule;
        private final String color;
        private final Map<String, Integer> containsOtherbags = new HashMap<>();
        private final Set<String> canBeContainedIn = new HashSet<>();

        public BagRule(String input) {
            rule = input;

            final Matcher matcher = rulePattern.matcher(input);
            if (matcher.matches()) {
                color = matcher.group(1);
                final String contains = matcher.group(2);
                for (String containPart : contains.split(", ")) {
                    final Matcher containsMatcher = containsPattern.matcher(containPart);
                    if (containsMatcher.matches()) {
                        final int amount = Integer.parseInt(containsMatcher.group(1));
                        final String color = containsMatcher.group(2);
                        containsOtherbags.put(color, amount);
                    }
                }
            } else {
                throw new IllegalArgumentException("Not valid rule: " + input);
            }
        }
    }
}
