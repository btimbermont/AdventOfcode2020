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
                .filter(Passport::requiredFieldsPresent)
                .count();
        System.out.println("valid passports: " + count);
        count = inputPassports.stream()
                .filter(Passport::requiredFieldsValid)
                .count();
        System.out.println("\nPart 2:");
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
        static final Pattern heightPattern = Pattern.compile("(\\d+)(cm|in)");
        static final List<String> validEcls = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

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

        public boolean requiredFieldsPresent() {
            return data.containsKey("byr")
                    && data.containsKey("iyr")
                    && data.containsKey("eyr")
                    && data.containsKey("hgt")
                    && data.containsKey("hcl")
                    && data.containsKey("ecl")
                    && data.containsKey("pid");
//            data.containsKey("cid"); // optional because we want north pole documents to be valid
        }

        public boolean requiredFieldsValid() {
            // Should be done differently: use a map of <String, Predicate> validators
            return requiredFieldsPresent()
                    && byrValid()
                    && iyrValid()
                    && eyrValid()
                    && hgtValid()
                    && hclValid()
                    && eclValid()
                    && pidValid();
        }

        public boolean byrValid() {
            boolean byrValid = isValidNumber(data.get("byr"), 1920, 2002);
//            if (!byrValid) System.err.println("Invalid birth year: " + data.get("byr"));
            return byrValid;
        }

        public boolean iyrValid() {
            boolean iyrValid = isValidNumber(data.get("iyr"), 2010, 2020);
//            if (!iyrValid) System.err.println("Invalid iyr: " + data.get("iyr"));
            return iyrValid;
        }

        public boolean eyrValid() {
            boolean eyrValid = isValidNumber(data.get("eyr"), 2020, 2030);
//            if (!eyrValid) System.err.println("Invalid eyr: " + data.get("eyr"));
            return eyrValid;
        }

        public boolean hgtValid() {
            boolean heightValid = false;
            try {
                Matcher matcher = heightPattern.matcher(data.get("hgt"));
                if (matcher.matches()) {
                    final int height = Integer.parseInt(matcher.group(1));
                    final String unit = matcher.group(2);
                    if (unit.equals("cm")) heightValid = height >= 150 && height <= 193;
                    if (unit.equals("in")) heightValid = height >= 59 && height <= 76;
                }
            } catch (Exception e) {
            }
//            if (!heightValid) System.err.println("Invalid height: " + data.get("hgt"));
            return heightValid;
        }

        public boolean hclValid() {
            boolean hclValid = data.containsKey("hcl") && data.get("hcl").matches("#[0-9a-f]{6}");
//            if (!hclValid) System.err.println("invalid hair color: " + data.get("hcl"));
            return hclValid;
        }

        public boolean eclValid() {
            boolean eclValid = validEcls.contains(data.get("ecl"));
//            if (!eclValid) System.err.println("invalid eye color: " + data.get("ecl"));
            return eclValid;
        }

        public boolean pidValid() {
            boolean pidValid = data.get("pid").matches("[0-9]{9}");
//            if (!pidValid) System.err.println("Invalid passport ID: " + data.get("pid"));
            return pidValid;
        }

        public boolean isValidNumber(final String value, final int min, final int max) {
            try {
                final int val = Integer.parseInt(value);
                return val >= min && val <= max;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        @Override
        public String toString() {
            return "Passport{" +
                    "data=" + data + '}';
        }
    }
}
