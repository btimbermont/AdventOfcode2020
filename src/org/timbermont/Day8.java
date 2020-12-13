package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.timbermont.Day8.InstructionType.*;

public class Day8 {

    private static List<Instruction> instructionsInput;
    private static int accumulator;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        runUntilLoop(instructionsInput);
        System.out.println("result: " + accumulator);

        System.out.println("Part 2:");
        // try changing every single instruction
        // 1: collect instructions to try and change
        final List<Integer> instructionsToTry = IntStream.range(0, instructionsInput.size())
                .filter(i -> {
                    InstructionType type = instructionsInput.get(i)._type;
                    return type == JMP || (type == NOP && instructionsInput.get(i)._value != 0);
                })
                .boxed()
                .collect(Collectors.toList());
        // 2: try all jmp and nop instructions until something succeeds
        for (Integer i : instructionsToTry) {
            // flip instruction i
            final List<Instruction> newInstructions = new ArrayList<>(instructionsInput);
            newInstructions.set(i, flip(instructionsInput.get(i)));
            // check result
            boolean noLoop = runUntilLoop(newInstructions);
            if (noLoop) {
                System.out.println("succes! changed " + (i + 1) + " result: " + accumulator);
                break;
            }
        }
    }

    public static Instruction flip(final Instruction i) {
        final InstructionType newType = i._type == NOP ? JMP : NOP;
        return new Instruction(newType, i._value);
    }

    public static boolean runUntilLoop(final List<Instruction> instructions) {
        final Set<Integer> processedInstructions = new HashSet<>();
        int currentInstructionIndex = 0;
        accumulator = 0;

        while (0 <= currentInstructionIndex
                && currentInstructionIndex < instructions.size()
                && !processedInstructions.contains(currentInstructionIndex)) {
            processedInstructions.add(currentInstructionIndex);
            final Instruction currentInstruction = instructions.get(currentInstructionIndex);
//            System.out.println("executing (" + currentInstructionIndex + "): " + currentInstruction);
            switch (currentInstruction._type) {
                case JMP:
                    currentInstructionIndex += currentInstruction._value;
                    break;
                case ACC:
                    accumulator += currentInstruction._value;
                default:
                    currentInstructionIndex++;
            }
        }
        // return whether the program finished
        return currentInstructionIndex == instructions.size();
    }

    public static void loadInput() throws URISyntaxException, IOException {
        instructionsInput = Files.lines(Path.of(Day8.class.getResource("Day8_input.txt").toURI()))
                .map(Instruction::new)
                .collect(Collectors.toList());
    }

    public static class Instruction {
        private final InstructionType _type;
        private final int _value;

        public Instruction(final String input) {
            final String[] split = input.split(" ");
            _type = InstructionType.valueOf(split[0].toUpperCase());
            _value = Integer.parseInt(split[1]);
        }

        public Instruction(InstructionType _type, int _value) {
            this._type = _type;
            this._value = _value;
        }

        @Override
        public String toString() {
            return "Instruction{_type=" + _type + ", _value=" + _value + '}';
        }
    }

    public enum InstructionType {
        JMP, ACC, NOP;
    }
}
