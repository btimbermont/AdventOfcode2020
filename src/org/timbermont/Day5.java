package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5 {
    static List<Seat> inputSeats;

    public static void main(final String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        final OptionalLong highestId = inputSeats.stream().mapToLong(Seat::getID).max();
        System.out.println("Highest ID: " + highestId);
    }

    static void loadInput() throws URISyntaxException, IOException {
        final Stream<String> lines = Files.lines(Path.of(Day1.class.getResource("Day5_input.txt").toURI()));
        inputSeats = lines.map(Seat::new).collect(Collectors.toList());
    }

    private static class Seat {
        private int row, column;

        public Seat(final String code) {
            row = getRowNumber(code);
            column = getColumnNumber(code);
        }

        public long getID() {
            return row * 8L + column;
        }

        @Override
        public String toString() {
            return "Seat{ row=" + row + ", column=" + column + ", ID =" + getID() + '}';
        }
    }

    private static int getRowNumber(final String seatCode) {
        final String rowNumberBinary = seatCode.substring(0, 7)
                .replaceAll("F", "0")
                .replaceAll("B", "1");
        return Integer.parseInt(rowNumberBinary, 2);
    }

    private static int getColumnNumber(final String seatCode) {
        final String rowNumberBinary = seatCode.substring(7)
                .replaceAll("L", "0")
                .replaceAll("R", "1");
        return Integer.parseInt(rowNumberBinary, 2);
    }
}
