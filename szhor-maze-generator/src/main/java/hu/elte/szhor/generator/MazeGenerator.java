package hu.elte.szhor.generator;

import hu.elte.szhor.generator.util.Coordinate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Random;

public class MazeGenerator {

    private static final Random RANDOM_GENERATOR = new Random();
    private static int graphSize;

    public static String[][] generate(final int size) {
        graphSize = size;
        var nodeCount = getRandomNumber(4 * size - 4, size * size);

        var availableCoordinates = new LinkedList<Coordinate>();
        var nodeCoordinates = new LinkedList<Coordinate>();

        for (var i = 0; i < graphSize ; ++i) {
            for (var j = 0; j < graphSize; ++j) {
                if (i == 0 || j == 0 || i == graphSize - 1 || j == graphSize - 1) {
                    nodeCoordinates.add(new Coordinate(i, j));
                } else {
                    availableCoordinates.add(new Coordinate(i, j));
                }
            }
        }

        var difference = nodeCount - nodeCoordinates.size();
        for (var i = 0; i < difference; ++i) {
            var generatedNodeCoordinate = getRandomNumber(0, availableCoordinates.size() - 1);
            nodeCoordinates.add(availableCoordinates.get(generatedNodeCoordinate));
            availableCoordinates.remove(generatedNodeCoordinate);
        }

        for (var i = 0; i < nodeCount; ++i) {
            var currentRow = nodeCoordinates.get(i).rowIndex;
            var currentColumn = nodeCoordinates.get(i).columnIndex;

            if (isIsolated(nodeCoordinates.get(i), nodeCoordinates)) {
                var nearestNeighbour = getNearestNeighbour(nodeCoordinates.get(i), nodeCoordinates);
                if (nearestNeighbour.columnIndex == currentColumn && nearestNeighbour.rowIndex < currentRow) {
                    for (var j = currentRow - 1; j > nearestNeighbour.rowIndex; --j) {
                        nodeCoordinates.add(new Coordinate(j, currentColumn));
                        availableCoordinates.remove(new Coordinate(j, currentColumn));
                    }
                } else if (nearestNeighbour.columnIndex == currentColumn && nearestNeighbour.rowIndex > currentRow) {
                    for (var j = currentRow + 1; j < nearestNeighbour.rowIndex; ++j) {
                        nodeCoordinates.add(new Coordinate(j, currentColumn));
                        availableCoordinates.remove(new Coordinate(j, currentColumn));
                    }
                } else if (nearestNeighbour.rowIndex == currentRow && nearestNeighbour.columnIndex < currentColumn) {
                    for (var j = currentColumn - 1; j > nearestNeighbour.columnIndex; --j) {
                        nodeCoordinates.add(new Coordinate(currentRow, j));
                        availableCoordinates.remove(new Coordinate(currentRow, j));
                    }
                } else if (nearestNeighbour.rowIndex == currentRow && nearestNeighbour.columnIndex > currentColumn) {
                    for (var j = currentColumn + 1; j < nearestNeighbour.columnIndex; ++j) {
                        nodeCoordinates.add(new Coordinate(currentRow, j));
                        availableCoordinates.remove(new Coordinate(currentRow, j));
                    }
                }
            }
        }

        var maze = new String[graphSize][graphSize];
        for (var i = 0; i < graphSize; ++i) {
            for (var j = 0; j < graphSize; ++j) {
                maze[i][j] = nodeCoordinates.contains(new Coordinate(i, j)) ? "*" : "#";
            }
        }

        return maze;
    }

    public static void saveToFile(final String filepath, final String[][] maze) throws FileNotFoundException {
        var sb = new StringBuilder();
        for (var strings : maze) {
            for (var string : strings) {
                sb.append(string);
            }
            sb.append("\n");
        }

        try (var writer = new PrintWriter(new File(filepath))) {
            writer.write(sb.toString());
            writer.flush();
        }
    }

    private static int getRandomNumber(final int origin, final int bound) {
        return RANDOM_GENERATOR.nextInt(bound - origin + 1) + origin;
    }

    private static boolean isIsolated(final Coordinate coordinate, final LinkedList<Coordinate> coordinates) {
        return !(
            coordinates.contains(new Coordinate(coordinate.rowIndex - 1, coordinate.columnIndex)) ||
            coordinates.contains(new Coordinate(coordinate.rowIndex + 1, coordinate.columnIndex)) ||
            coordinates.contains(new Coordinate(coordinate.rowIndex, coordinate.columnIndex - 1)) ||
            coordinates.contains(new Coordinate(coordinate.rowIndex, coordinate.columnIndex + 1))
        );
    }

    private static Coordinate getNearestNeighbour(final Coordinate coordinate, final LinkedList<Coordinate> coordinates) {
        var row = coordinate.rowIndex;
        var column = coordinate.columnIndex;
        var nearestNeighbour = new Coordinate(row, column);

        // Nearest neighbour from upside
        for (int i = row - 1; i >= 0; --i) {
            if (coordinates.contains(new Coordinate(i, column))) {
                nearestNeighbour.rowIndex = i;
                break;
            }
        }

        // Nearest neighbour from downside
        for (int i = row + 1; i < graphSize; ++i) {
            if (coordinates.contains(new Coordinate(i, column))) {
                if (Math.abs(row - nearestNeighbour.rowIndex) > Math.abs(row - i)) {
                    nearestNeighbour.rowIndex = i;
                }
                break;
            }
        }

        // Nearest neighbour to the left
        for (int j = column - 1; j >= 0; --j) {
            if (coordinates.contains(new Coordinate(row, j))) {
                if (Math.abs(row - nearestNeighbour.rowIndex) > Math.abs(column - j)) {
                    nearestNeighbour.columnIndex = j;
                    nearestNeighbour.rowIndex = row;
                }
                break;
            }
        }

        // Nearest neighbour to the right
        for (int j = column + 1; j < graphSize; ++j) {
            if (coordinates.contains(new Coordinate(row, j))) {
                if (nearestNeighbour.rowIndex != row && Math.abs(row - nearestNeighbour.rowIndex) > Math.abs(column - j)) {
                    nearestNeighbour.columnIndex = j;
                    nearestNeighbour.rowIndex = row;
                } else if (Math.abs(column - nearestNeighbour.columnIndex) > Math.abs(column - j)) {
                    nearestNeighbour.columnIndex = j;
                }
                break;
            }
        }

        return nearestNeighbour;
    }
}
