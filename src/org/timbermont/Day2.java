package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path; 
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 {
    static List<PasswordEntry> input;

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("part 1:");
        loadInput();
        long validPasswords = input.stream().filter(PasswordEntry::isValid).count();
        System.out.println("Valid passwords: " + validPasswords);

        System.out.println("part 2:");
        long validPasswords2 = input.stream().filter(PasswordEntry::isValid2).count();
        System.out.println("Valid passwords: " + validPasswords2);

    }

    private static void loadInput() throws URISyntaxException, IOException {
        Stream<String> lines = Files.lines(Path.of(Day1.class.getResource("Day2_input.txt").toURI()));
        input = lines
                .filter(s -> s.contains(": "))
                .map(PasswordEntry::new)
                .collect(Collectors.toCollection(ArrayList::new));
        lines.close();
    }

    static class PasswordEntry {
        int policyLower;
        int policyUpper;
        char policyCharacter;
        String password;

        static final Pattern entrypattern = Pattern.compile("(\\d+)-(\\d+) ([\\w]): (.*)");

        public PasswordEntry(String lineEntry) {
            Matcher m = entrypattern.matcher(lineEntry);

            if (m.find()) {
                policyLower = Integer.parseInt(m.group(1));
                policyUpper = Integer.parseInt(m.group(2));
                policyCharacter = m.group(3).charAt(0);
                password = m.group(4);
            } else {
                System.err.println("Could not parse line: " + lineEntry);
            }
        }

        public boolean isValid() {
            int count = 0;
            for (char c : password.toCharArray()) {
                if (c == policyCharacter) count++;
            }
            return count >= policyLower && count <= policyUpper;
        }

        public boolean isValid2() {
            try {
                char char1 = password.charAt(policyLower - 1);
                char char2 = password.charAt(policyUpper - 1);
                return (char1 == policyCharacter) ^ (char2 == policyCharacter);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("invalid shit with indexes: " + this);
                return false;
            }
        }

        @Override
        public String toString() {
            return "PasswordEntry{ policy: " + policyLower +
                    "-" + policyUpper +
                    "x" + policyCharacter + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
