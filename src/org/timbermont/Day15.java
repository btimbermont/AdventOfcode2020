package org.timbermont;

import java.util.*;

public class Day15 {

    private static final List<Integer> input = List.of(5, 1, 9, 18, 13, 8, 0);

    public static void main(String[] args) {
        System.out.println("Part 1;");

        final IndexedSequence indexedSequence = new IndexedSequence(input);
        while (indexedSequence.getSize() < 2020) {
            indexedSequence.generateNextValue();
        }
        System.out.println("2020th number: " + lastValue(indexedSequence.getSequence()));

        System.out.println("Part 2;");
        while (indexedSequence.getSize() < 30_000_000) {
            indexedSequence.generateNextValue();
        }
        System.out.println("30 millionth number: " + lastValue(indexedSequence.getSequence()));
    }


    private static Integer lastValue(final List<Integer> list) {
        if (list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }

    private static class IndexedSequence {
        private final List<Integer> sequence = new ArrayList<>();
        private final Map<Integer, List<Integer>> indexMap = new HashMap<>();

        private IndexedSequence(final List<Integer> startingSequence) {
            sequence.addAll(startingSequence);
            // index starting numbers
            int index = 0;
            for (Integer startingNumber : startingSequence) {
                getIndexesForValue(startingNumber).add(index);
                index++;
            }
        }

        private List<Integer> getIndexesForValue(final int value) {
            return indexMap.computeIfAbsent(value, v -> new ArrayList<>());
        }

        public List<Integer> getSequence() {
            return Collections.unmodifiableList(sequence);
        }

        public int getSize() {
            return sequence.size();
        }

        public void generateNextValue() {
            final int lastValue = lastValue(sequence);
            final List<Integer> indexesForValue = getIndexesForValue(lastValue);
            final int nextValue;
            if (indexesForValue.size() == 1) {
                nextValue = 0;
            } else {
                final int lastIndex = indexesForValue.get(indexesForValue.size() - 1);
                final int previousIndex = indexesForValue.get(indexesForValue.size() - 2);
                nextValue = lastIndex - previousIndex;
            }
            // insert new value
            int nextValueIndex = sequence.size();
            sequence.add(nextValue);
            getIndexesForValue(nextValue).add(nextValueIndex);
        }

        @Override
        public String toString() {
            return "IndexedSequence{" +
                    "sequence=" + sequence +
                    ", indexMap=" + indexMap +
                    '}';
        }
    }

}
