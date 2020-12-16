package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

        System.out.println("\n\nPart 2: ");
        //reset floorplan
        loadInput();

        i = 0;
        while (updateFloorplan2()) {
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
        for (int x = i - 1; x <= i + 1; x++) {
            for (int y = j - 1; y <= j + 1; y++) {
                if (isOutOfBounds(x, y) || (i == x && j == y)) continue;
                if (floorPlan[x][y] == TAKEN) takenSeats++;
            }
        }
        return takenSeats;
    }

    private static boolean updateFloorplan2() {
        char[][] newFloorplan = copyFloorplan();
        boolean somethingChanged = false;
        for (int i = 0; i < floorPlan.length; i++) {
            for (int j = 0; j < floorPlan[i].length; j++) {
                switch (floorPlan[i][j]) {
                    case EMPTY:
                        if (takenVisibleSeats(i, j) == 0) {
                            newFloorplan[i][j] = TAKEN;
                            somethingChanged = true;
                        }
                        break;
                    case TAKEN:
                        if (takenVisibleSeats(i, j) >= 5) {
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

    private static int takenVisibleSeats(final int i, final int j) {
        int visibleTakenSeats = 0;
        for (int dirX = -1; dirX <= 1; dirX++) {
            for (int dirY = -1; dirY <= 1; dirY++) {
                if (dirX == 0 && dirY == 0) continue;
                if (seesTakenSeatInDirection(i, j, dirX, dirY)) {
                    visibleTakenSeats++;
                }
            }
        }
        return visibleTakenSeats;
    }

    private static boolean seesTakenSeatInDirection(final int posX, final int posY, final int dirX, final int dirY) {
        final int watchX = posX + dirX;
        final int watchY = posY + dirY;
        if (isOutOfBounds(watchX, watchY) || floorPlan[watchX][watchY] == EMPTY) return false;
        if (floorPlan[watchX][watchY] == TAKEN) return true;
        return seesTakenSeatInDirection(watchX, watchY, dirX, dirY);
    }

    private static boolean isOutOfBounds(int watchX, int watchY) {
        return watchX < 0 || watchX >= floorPlan.length || watchY < 0 || watchY >= floorPlan[watchX].length;
    }

    private static int countTakenSeats() {
        int occupied = 0;
        for (char[] row : floorPlan) {
            for (char position : row) {
                if (position == TAKEN) occupied++;
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
