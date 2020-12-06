package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {

    private static List<Passport> inputPassports;

    public static void main(final String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        long count = inputPassports.stream()
                .filter(Passport::isValid)
                .count();
        System.out.println("valid passports: " + count);
    }

    public static void loadInput() throws URISyntaxException, IOException {
        final List<String> lines = Files.readAllLines(Path.of(Day1.class.getResource("Day4_input.txt").toURI()));
        inputPassports = new ArrayList<>();
        Passport currentPassport = new Passport();
        for (String line : lines) {
            // guard clause for starting a new passport on a blank line
            if (line.isBlank()) {
                inputPassports.add(currentPassport);
                currentPassport = new Passport();
                continue;
            }
            // regular case: add each line to the current passport
            currentPassport.addData(line);
        }
    }

    private static class Passport {
        private Map<String, String> data;

        static final Pattern dataPattern = Pattern.compile("(\\w+):([^\\s]+)");

        public Passport() {
            data = new HashMap<>();
        }

        public void addData(final String newData) {
            // split by whitespace
            for (String dataPiece : newData.split("\\s+")) {
                Matcher matcher = dataPattern.matcher(dataPiece);
                if (matcher.matches()) {
                    data.put(matcher.group(1), matcher.group(2));
                } else {
                    System.err.println(String.format("could not match piece '%s', part of data '%s'", dataPiece, newData));
                }
            }
        }

        public boolean isValid() {
            return data.containsKey("byr")
                    && data.containsKey("iyr")
                    && data.containsKey("eyr")
                    && data.containsKey("hgt")
                    && data.containsKey("hcl")
                    && data.containsKey("ecl")
                    && data.containsKey("pid");
//            data.containsKey("cid"); // optional because we want north pole documents to be valid
        }

        @Override
        public String toString() {
            return "Passport{" +
                    "data=" + data + '}';
        }
    }
}
