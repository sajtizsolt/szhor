package hu.elte.szhor.generator;

import hu.elte.szhor.generator.util.Coordinate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class MazeGenerator {

    private MazeGenerator() {
    }

    public static String[][] generate(final int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Maze size must be at least 2, but was: " + size);
        }
        var nodeCount = getRandomNumber(4 * size - 4, size * size);

        var availableCoordinates = new ArrayList<Coordinate>();
        var borderCoordinates = new ArrayList<Coordinate>();
        var coordinateSet = new HashSet<Coordinate>();
        var connectedSet = new HashSet<Coordinate>();

        for (var i = 0; i < size; ++i) {
            for (var j = 0; j < size; ++j) {
                var coord = new Coordinate(i, j);
                if (i == 0 || j == 0 || i == size - 1 || j == size - 1) {
                    borderCoordinates.add(coord);
                    coordinateSet.add(coord);
                    connectedSet.add(coord);
                } else {
                    availableCoordinates.add(coord);
                }
            }
        }

        var interiorCoordinates = new ArrayList<Coordinate>();
        var difference = nodeCount - borderCoordinates.size();
        if (difference > 0) {
            Collections.shuffle(availableCoordinates);
            for (var i = 0; i < difference; ++i) {
                var coord = availableCoordinates.get(i);
                interiorCoordinates.add(coord);
                coordinateSet.add(coord);
            }
        }

        for (var current : interiorCoordinates) {
            if (connectedSet.contains(current)) {
                continue;
            }
            if (isAdjacentTo(current, connectedSet)) {
                connectedSet.add(current);
                continue;
            }

            var nearest = getNearestNeighbour(current, connectedSet, size);
            var currentRow = current.rowIndex();
            var currentColumn = current.columnIndex();

            if (nearest.columnIndex() == currentColumn && nearest.rowIndex() < currentRow) {
                for (var j = currentRow; j > nearest.rowIndex(); --j) {
                    var c = new Coordinate(j, currentColumn);
                    coordinateSet.add(c);
                    connectedSet.add(c);
                }
            } else if (nearest.columnIndex() == currentColumn && nearest.rowIndex() > currentRow) {
                for (var j = currentRow; j < nearest.rowIndex(); ++j) {
                    var c = new Coordinate(j, currentColumn);
                    coordinateSet.add(c);
                    connectedSet.add(c);
                }
            } else if (nearest.rowIndex() == currentRow && nearest.columnIndex() < currentColumn) {
                for (var j = currentColumn; j > nearest.columnIndex(); --j) {
                    var c = new Coordinate(currentRow, j);
                    coordinateSet.add(c);
                    connectedSet.add(c);
                }
            } else if (nearest.rowIndex() == currentRow && nearest.columnIndex() > currentColumn) {
                for (var j = currentColumn; j < nearest.columnIndex(); ++j) {
                    var c = new Coordinate(currentRow, j);
                    coordinateSet.add(c);
                    connectedSet.add(c);
                }
            }
        }

        var maze = new String[size][size];
        for (var i = 0; i < size; ++i) {
            for (var j = 0; j < size; ++j) {
                maze[i][j] = coordinateSet.contains(new Coordinate(i, j)) ? "*" : "#";
            }
        }

        return maze;
    }

    public static void saveToFile(final String filepath, final String[][] maze) throws IOException {
        var sb = new StringBuilder();
        for (var row : maze) {
            for (var cell : row) {
                sb.append(cell);
            }
            sb.append("\n");
        }
        var path = Path.of(filepath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, sb.toString(), StandardCharsets.UTF_8);
    }

    private static int getRandomNumber(final int origin, final int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound + 1);
    }

    private static boolean isAdjacentTo(final Coordinate coordinate, final Set<Coordinate> coordinates) {
        return coordinates.contains(new Coordinate(coordinate.rowIndex() - 1, coordinate.columnIndex()))
            || coordinates.contains(new Coordinate(coordinate.rowIndex() + 1, coordinate.columnIndex()))
            || coordinates.contains(new Coordinate(coordinate.rowIndex(), coordinate.columnIndex() - 1))
            || coordinates.contains(new Coordinate(coordinate.rowIndex(), coordinate.columnIndex() + 1));
    }

    private static Coordinate getNearestNeighbour(final Coordinate coordinate, final Set<Coordinate> coordinates, final int size) {
        final int row = coordinate.rowIndex();
        final int column = coordinate.columnIndex();
        Coordinate nearest = null;
        int minDistance = Integer.MAX_VALUE;

        // Nearest neighbour from upside
        for (int i = row - 1; i >= 0; --i) {
            var c = new Coordinate(i, column);
            if (coordinates.contains(c)) {
                int dist = row - i;
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = c;
                }
                break;
            }
        }

        // Nearest neighbour from downside
        for (int i = row + 1; i < size; ++i) {
            var c = new Coordinate(i, column);
            if (coordinates.contains(c)) {
                int dist = i - row;
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = c;
                }
                break;
            }
        }

        // Nearest neighbour to the left
        for (int j = column - 1; j >= 0; --j) {
            var c = new Coordinate(row, j);
            if (coordinates.contains(c)) {
                int dist = column - j;
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = c;
                }
                break;
            }
        }

        // Nearest neighbour to the right
        for (int j = column + 1; j < size; ++j) {
            var c = new Coordinate(row, j);
            if (coordinates.contains(c)) {
                int dist = j - column;
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = c;
                }
                break;
            }
        }

        return nearest != null ? nearest : coordinate;
    }
}
