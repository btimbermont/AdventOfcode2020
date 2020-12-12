package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day6 {

    private static List<Group> groups;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("part 1:");
        final long sum = groups.stream()
                .mapToLong(group -> group.uniqueYesAnswers.size())
                .sum();
        System.out.println(sum);

        System.out.println("Part 2:");
        long sum2 = groups.stream()
                .mapToLong(group -> group.commonYesAnswer.size())
                .sum();
        System.out.println(sum2);
    }

    public static void loadInput() throws URISyntaxException, IOException {
        final List<String> lines = Files.readAllLines(Path.of(Day6.class.getResource("Day6_input.txt").toURI()));
        groups = new ArrayList<>();
        Group currentGroup = new Group();
        for (String line : lines) {
            if (line.isBlank()) {
                groups.add(currentGroup);
                currentGroup = new Group();
            } else {
                currentGroup.addPerson(line);
            }
        }

        if (currentGroup.allAnswerSets.size() > 0) groups.add(currentGroup);
    }

    public static class Group {
        public List<PersonalAnwsers> allAnswerSets = new ArrayList<>();
        public Set<Character> uniqueYesAnswers = new HashSet<>();
        public Set<Character> commonYesAnswer = null;

        public void addPerson(final String input) {
            final PersonalAnwsers newPerson = new PersonalAnwsers(input);
            allAnswerSets.add(newPerson);
            uniqueYesAnswers.addAll(newPerson.yesAnswers);
            processCommonAnswers(newPerson);
        }

        private void processCommonAnswers(final PersonalAnwsers newPerson) {
            if (commonYesAnswer == null) {
                commonYesAnswer = Collections.unmodifiableSet(newPerson.yesAnswers);
            } else {
                commonYesAnswer = newPerson.yesAnswers.stream()
                        .filter(ch -> commonYesAnswer.contains(ch))
                        .collect(Collectors.toSet());
            }
        }
    }

    public static class PersonalAnwsers {
        final Set<Character> yesAnswers;

        public PersonalAnwsers(final String input) {
            yesAnswers = new HashSet<>();
            for (char c : input.toCharArray()) {
                yesAnswers.add(c);
            }
        }
    }
}
