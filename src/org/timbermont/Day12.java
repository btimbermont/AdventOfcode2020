package org.timbermont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {

    private static List<Instruction> input;
    private static final Map<Direction, Direction> RIGHT_TURN_MAP = Map.of(
            Direction.NORTH, Direction.EAST,
            Direction.EAST, Direction.SOUTH,
            Direction.SOUTH, Direction.WEST,
            Direction.WEST, Direction.NORTH);

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadInput();

        System.out.println(input);

        System.out.println("Part 1:");
        applyInstructions();

        System.out.println("Part 2:");
        ShipPosition2 position = new ShipPosition2();
        for (Instruction instruction : input) {
            position.interpretInstruction(instruction);
        }
        System.out.println("position after new isntructions: " + position);
        System.out.println("Manhattan distance: " + (Math.abs(position._x) + Math.abs(position._y)));
    }

    private static void applyInstructions() {
        ShipPosition currentPosition = new ShipPosition();
        for (Instruction instruction : input) {
            instruction.accept(currentPosition);
        }

        // taximetric
        System.out.println("Manhattan distance from start: " + (Math.abs(currentPosition._x) + Math.abs(currentPosition._y)));
    }

    private static void loadInput() throws URISyntaxException, IOException {
        final Stream<String> lines = Files.lines(Path.of(Day12.class.getResource("Day12_input.txt").toURI()));
        input = lines.map(Instruction::new)
                .collect(Collectors.toList());
    }

    private static class Instruction implements Consumer<ShipPosition> {
        private final InstructionType type;
        private final int value;

        public Instruction(String input) {
            this.type = InstructionType.valueOf(input.substring(0, 1));
            this.value = Integer.parseInt(input.substring(1));
        }

        @Override
        public String toString() {
            return "Instruction {" + type + "," + value + '}';
        }

        @Override
        public void accept(ShipPosition shipPosition) {
            type.applyWithValue(shipPosition, value);
        }
    }

    private static enum InstructionType {
        N((position, i) -> position.moveInDirection(Direction.NORTH, i)),
        E((position, i) -> position.moveInDirection(Direction.EAST, i)),
        S((position, i) -> position.moveInDirection(Direction.SOUTH, i)),
        W((position, i) -> position.moveInDirection(Direction.WEST, i)),
        F((shipPosition, i) -> shipPosition.moveInDirection(shipPosition._direction, i)),
        R((shipPosition, i) -> {
            // make sure i in [0,360[
            while (i < 360) i += 360;
            // turn in 90 degree steps
            Direction currentDirection = shipPosition._direction;
            while (i > 0) {
                currentDirection = RIGHT_TURN_MAP.get(currentDirection);
                i -= 90;
            }
            shipPosition._direction = currentDirection;
        }),
        L((shipPosition, i) -> {
            R.applyWithValue(shipPosition, i * -1);
        });

        private final BiConsumer<ShipPosition, Integer> delegate;

        InstructionType(BiConsumer<ShipPosition, Integer> delegate) {
            this.delegate = delegate;
        }

        public void applyWithValue(ShipPosition shipPosition, int instructionValue) {
            delegate.accept(shipPosition, instructionValue);
        }
    }

    private static class ShipPosition {
        private int _x, _y;
        private Direction _direction;

        public ShipPosition() {
            // start at 0,0
            _x = 0;
            _y = 0;
            // start pointing east
            _direction = Direction.EAST;
        }

        public void moveInDirection(Direction direction, int amount) {
            _x += direction.dirX * amount;
            _y += direction.dirY * amount;
        }

        @Override
        public String toString() {
            return "{" + _x + "," + _y + " direction: " + _direction + '}';
        }
    }

    private enum Direction {
        NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);
        private final int dirX, dirY;

        Direction(int dirX, int dirY) {
            this.dirX = dirX;
            this.dirY = dirY;
        }
    }

    private static class ShipPosition2 {
        private int _x = 0, _y = 0;
        private int _waypointX = 10, _waypointY = 1;

        public void moveWaypoint(final int dx, int dy) {
            _waypointX += dx;
            _waypointY += dy;
        }

        public void rotateLeft() {
            int newWaypointX = Math.abs(_waypointY);
            int newWaypointY = Math.abs(_waypointX);

            if (_waypointY > 0) {
                newWaypointX *= -1;
            }
            if (_waypointX < 0) {
                newWaypointY *= -1;
            }

            _waypointX = newWaypointX;
            _waypointY = newWaypointY;
        }

        public void rotateRight() {
            rotateLeft();
            rotateLeft();
            rotateLeft();
        }

        public void moveToWaypoint() {
            _x += _waypointX;
            _y += _waypointY;
        }

        public void interpretInstruction(final Instruction instruction) {
            switch (instruction.type) {
                case N:
                    moveWaypoint(0, instruction.value);
                    break;
                case E:
                    moveWaypoint(instruction.value, 0);
                    break;
                case S:
                    moveWaypoint(0, instruction.value * -1);
                    break;
                case W:
                    moveWaypoint(instruction.value * -1, 0);
                    break;
                case F:
                    for (int i = 0; i < instruction.value; i++) {
                        this.moveToWaypoint();
                    }
                    break;
                case L:
                    for (int i = 0; i < instruction.value; i += 90) {
                        this.rotateLeft();
                    }
                    break;
                case R:
                    for (int i = 0; i < instruction.value; i += 90) {
                        this.rotateRight();
                    }
                    break;
                default:
                    System.err.println("should not happen");
                    break;

            }
        }

        @Override
        public String toString() {
            return "{position:" + _x + "," + _y + " waypoint: " + _waypointX + "," + _waypointY + '}';
        }
    }
}
