package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.Math.*;

public class Day11 {

    static final char EMPTY = 'L';
    static final char TAKEN = '#';
    static final char FLOOR = '.';

    static char[][] floorPlan;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println("Part 1:");
        int i = 0;
        while (updateFloorPlan()) {
            i++;
        }
        System.out.println("Updated " + i + " times before stabilizing to this:");
        printFloorPlan();
        System.out.println("Free seats: " + countTakenSeats());
    }

    private static boolean updateFloorPlan() {
        char[][] newFloorplan = copyFloorplan();
        boolean somethingChanged = false;
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                switch (floorPlan[i][j]) {
                    case EMPTY:
                        if (takenAdjacentSeats(i, j) == 0) {
                            newFloorplan[i][j] = TAKEN;
                            somethingChanged = true;
                        }
                        break;
                    case TAKEN:
                        if (takenAdjacentSeats(i, j) >= 4) {
                            newFloorplan[i][j] = EMPTY;
                            somethingChanged = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        if (somethingChanged) {
            floorPlan = newFloorplan;
        }
        return somethingChanged;
    }

    private static int takenAdjacentSeats(final int i, final int j) {
        int takenSeats = 0;
        for (int a = max(0, i - 1); a <= min(i + 1, floorPlan.length - 1); a++) {
            for (int b = max(0, j - 1); b <= min(j + 1, floorPlan[a].length - 1); b++) {
                if (i == a && j == b) continue;
                if (floorPlan[a][b] == TAKEN) takenSeats++;
            }
        }
        return takenSeats;
    }

    private static int countTakenSeats(){
        int occupied = 0;
        for (char[] row : floorPlan) {
            for (char position : row) {
                if(position == TAKEN) occupied++;
            }
        }
        return occupied;
    }

    private static char[][] copyFloorplan() {
        char[][] newFloorplan = new char[floorPlan.length][floorPlan[0].length];
        for (int i = 0; i < newFloorplan.length; i++) {
            for (int j = 0; j < newFloorplan[0].length; j++) {
                newFloorplan[i][j] = floorPlan[i][j];
            }
        }
        return newFloorplan;
    }

    private static void printFloorPlan() {
        System.out.println("-----");
        for (char[] row : floorPlan) {
            System.out.println(String.valueOf(row));
        }
        System.out.println("-----");
    }

    private static void loadInput() throws URISyntaxException, IOException {
        final List<String> lines = Files.readAllLines(Path.of(Day11.class.getResource("Day11_input.txt").toURI()));
        floorPlan = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            floorPlan[i] = new char[lines.get(i).length()];
            for (int j = 0; j < lines.get(i).length(); j++) {
                floorPlan[i][j] = lines.get(i).charAt(j);
            }
        }
    }
}
