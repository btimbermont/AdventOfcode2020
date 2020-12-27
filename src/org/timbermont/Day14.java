package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static java.lang.Long.toBinaryString;

public class Day14 {

    private static List<Instruction> input;
    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("(mem|mask)(\\[\\d+\\])? = (.*)");

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        processInput1();

        System.out.println("Part 2:");
        processInput2();

    }

    private static void loadInput() throws URISyntaxException, IOException {
        input = Files.lines(Path.of(Day14.class.getResource("Day14_input.txt").toURI()))
                .map(Instruction::forInput)
                .collect(Collectors.toList());
    }

    public static void processInput1() {
        Map<Long, Long> memory = new HashMap<>();
        Bitmask currentBitmask = new Bitmask("X"); // not really needed, but still
        for (Instruction instruction : input) {
            switch (instruction._type) {
                case MASK:
                    currentBitmask = instruction._bitmask;
                    break;
                case MEM:
                    final long valueToStore = currentBitmask.apply(instruction._memoryValue);
                    memory.put(instruction._memoryAddress, valueToStore);
            }
        }
        System.out.println("resulting memory: " + memory);
        long total = 0;
        for (Long value : memory.values()) {
            total += value;
        }
        System.out.println("Sum of all values: " + total);
    }


    private static void processInput2() {
        Map<Long, Long> memory = new HashMap<>();
        Bitmask currentBitmask = new Bitmask("0");
        for (Instruction instruction : input) {
            switch (instruction._type) {
                case MASK:
                    currentBitmask = instruction._bitmask;
                    break;
                case MEM:
                    final String floatingAddress = currentBitmask.apply2(instruction._memoryAddress);
                    final List<Long> allAddresses = expandFloatingAddress(floatingAddress);
                    for (Long address : allAddresses) {
                        memory.put(address, instruction._memoryValue);
                    }
            }
        }
        System.out.println("resulting memory: " + memory);
        long total = 0;
        for (Long value : memory.values()) {
            total += value;
        }
        System.out.println("Sum of all values: " + total);
    }

    private static List<Long> expandFloatingAddress(final String floatingAddress) {
        int firstXidx = floatingAddress.indexOf('X');
        if (firstXidx < 0) return List.of(Long.parseLong(floatingAddress, 2));
        final String prefix = floatingAddress.substring(0, firstXidx);
        final String suffix = floatingAddress.substring(firstXidx + 1);
        List<Long> result = new ArrayList<>();
        result.addAll(expandFloatingAddress(prefix + "0" + suffix));
        result.addAll(expandFloatingAddress(prefix + "1" + suffix));
        return Collections.unmodifiableList(result);
    }

    private static class Instruction {
        private final InstructionType _type;
        private final long _memoryAddress;
        private final long _memoryValue;
        private final Bitmask _bitmask;

        private Instruction(InstructionType _type, long _memoryAddress, long _memoryValue, Bitmask _bitmask) {
            this._type = _type;
            this._memoryAddress = _memoryAddress;
            this._memoryValue = _memoryValue;
            this._bitmask = _bitmask;
        }

        static Instruction forInput(final String input) {
            Matcher matcher = INSTRUCTION_PATTERN.matcher(input);
            if (matcher.matches()) {
                switch (matcher.group(1)) {
                    case "mask":
                        return mask(matcher.group(3));
                    case "mem":
                        final long address = Long.parseLong(matcher.group(2).substring(1, matcher.group(2).length() - 1));
                        final long value = Long.parseLong(matcher.group(3));
                        return store(address, value);
                }
            }
            throw new IllegalArgumentException("Invalid input: " + input);
        }

        static Instruction store(final long address, final long value) {
            return new Instruction(InstructionType.MEM, address, value, null);
        }

        static Instruction mask(final String bitmask) {
            return new Instruction(InstructionType.MASK, 0, 0, new Bitmask(bitmask));
        }
    }

    private enum InstructionType {
        MASK, MEM;
    }

    private static class Bitmask {
        final long _andMask, _orMask;
        final String _originalMask;

        public Bitmask(final String input) {
            _originalMask = leftPad(input, 'X');
            _orMask = parseLong(_originalMask.replace('X', '0'), 2);
            _andMask = parseLong(_originalMask.replace('X', '1'), 2);
        }

        public long apply(final long value) {
            long newValue = value | _orMask;
            newValue = newValue & _andMask;
            return newValue;
        }

        public String apply2(final Long address) {
            final String addressString = leftPad(Long.toBinaryString(address), '0');
            if (addressString.length() != _originalMask.length())
                throw new IllegalStateException("bitmask and address are of different lengths!\n" +
                        "bitmask:\t" + _originalMask + "\n" +
                        "address:\t" + addressString);
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < addressString.length(); i++) {
                switch (_originalMask.charAt(i)) {
                    case '0':
                        result.append(addressString.charAt(i));
                        break;
                    case '1':
                        result.append('1');
                        break;
                    case 'X':
                        result.append('X');
                        break;
                }
            }
            return result.toString();
        }

        @Override
        public String toString() {
            return "Bitmask{" +
                    "_andMask=" + toBinaryString(_andMask) +
                    ", _orMask=" + toBinaryString(_orMask) +
                    '}';
        }

        private static String leftPad(final String input, char paddingCharacter) {
            final StringBuilder result = new StringBuilder(input);
            while (result.length() < 36) {
                result.insert(0, paddingCharacter);
            }
            return result.toString();
        }
    }
}
