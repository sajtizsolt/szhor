package hu.elte.szhor.model;

import hu.elte.szhor.util.Coordinate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MazeGenerator {

    //private static final Logger LOGGER = LoggerFactory.getLogger(hu.elte.szhor.model.MazeGenerator.class);

    private final int maxRow;
    private final int maxColumn;
    private final int nodeNumber;
    private final String fileName;
    private final String filePath;

    public MazeGenerator(int maxRow, int maxColumn, String fileName) {
        this.maxRow = maxRow;
        this.maxColumn = maxColumn;
        this.nodeNumber = getRandomNumberInRange((maxRow * 2) + (maxColumn * 2) - 4, maxRow * maxColumn);
        this.fileName = fileName;
        this.filePath = fileName;
    }

    public MazeGenerator(int maxRow, int maxColumn) {
        this.maxRow = maxRow;
        this.maxColumn = maxColumn;
        this.nodeNumber = getRandomNumberInRange((maxRow * 2) + (maxColumn * 2) - 4, maxRow * maxColumn);
        this.fileName = ".\\szhor-simulator\\src\\main\\resources\\generated_maze";
        this.filePath = "szhor\\szhor-simulator\\src\\main\\resources\\generated_maze";
    }

    public void generateMaze() {
        try (var bufferedWriter = new BufferedWriter(new FileWriter(this.fileName))) {
            List<Coordinate> availableCoordinates = new ArrayList<>();
            List<Coordinate> nodeCoordinates = new ArrayList<>();

            //LOGGER.info("Generating maze with {} rows and {} columns.", maxRow, maxColumn);
            System.out.println("Generating maze with " + maxRow + "rows and" + maxColumn + " columns.");

            for(int i = 0; i < maxRow ; ++i){
                for(int j = 0; j < maxColumn; ++j){
                    if(i == 0 || j == 0 || i == maxRow - 1 || j == maxColumn - 1){
                        nodeCoordinates.add(new Coordinate(i, j));
                    } else {
                        availableCoordinates.add(new Coordinate(i, j));
                    }
                }
            }
            //LOGGER.info("Created frame.");
            System.out.println("Created frame.");

            //LOGGER.info("Generating random nodes inside the frame: ");
            System.out.println("Generating random nodes inside the frame.");
            var difference = nodeNumber - nodeCoordinates.size();
            for(int i = 0; i < difference; ++i){
                var generatedNodeCoordinate = getRandomNumberInRange(0, availableCoordinates.size() - 1);
                /*LOGGER.info("\t Adding random node ({}, {})",
                        availableCoordinates.get(generatedNodeCoordinate).rowIndex,
                        availableCoordinates.get(generatedNodeCoordinate).columnIndex);*/
                System.out.println("Adding random node. ");
                nodeCoordinates.add(availableCoordinates.get(generatedNodeCoordinate));
                availableCoordinates.remove(generatedNodeCoordinate);
            }

            for(int i = 0; i < nodeNumber; ++i){
                int currentRow = nodeCoordinates.get(i).rowIndex;
                int currentColumn = nodeCoordinates.get(i).columnIndex;

                if(isIsolated(nodeCoordinates.get(i), nodeCoordinates)){
                    //LOGGER.info("Node with coordinates ({}, {}) is isolated!", currentRow, currentColumn);
                    System.out.println("Isolated node!");
                    var nearestNeighbour = findNearestNeighbour(nodeCoordinates.get(i), nodeCoordinates);
                    /*LOGGER.info("The nearest neighbour for ({}, {}) is: ({}, {}).",
                            currentRow, currentColumn,
                            nearestNeighbour.rowIndex, nearestNeighbour.columnIndex
                    );*/

                    /*LOGGER.info("Creating path from ({}, {}) to ({}, {}):",
                            currentRow, currentColumn,
                            nearestNeighbour.rowIndex, nearestNeighbour.columnIndex
                    );*/
                    if(nearestNeighbour.columnIndex == currentColumn && nearestNeighbour.rowIndex < currentRow){
                        for(int j = currentRow - 1; j > nearestNeighbour.rowIndex; --j){
                            //LOGGER.info("\t Adding node ({}, {})", j, currentColumn);
                            nodeCoordinates.add(new Coordinate(j, currentColumn));
                            availableCoordinates.remove(new Coordinate(j, currentColumn));
                        }
                    } else if(nearestNeighbour.columnIndex == currentColumn && nearestNeighbour.rowIndex > currentRow){
                        for(int j = currentRow + 1; j < nearestNeighbour.rowIndex; ++j){
                            //LOGGER.info("\t Adding node ({}, {})", j, currentColumn);
                            nodeCoordinates.add(new Coordinate(j, currentColumn));
                            availableCoordinates.remove(new Coordinate(j, currentColumn));
                        }
                    } else if(nearestNeighbour.rowIndex == currentRow && nearestNeighbour.columnIndex < currentColumn){
                        for(int j = currentColumn - 1; j > nearestNeighbour.columnIndex; --j){
                            //LOGGER.info("\t Adding node ({}, {})", currentRow, j);
                            nodeCoordinates.add(new Coordinate(currentRow, j));
                            availableCoordinates.remove(new Coordinate(currentRow, j));
                        }
                    } else if(nearestNeighbour.rowIndex == currentRow && nearestNeighbour.columnIndex > currentColumn){
                        for(int j = currentColumn + 1; j < nearestNeighbour.columnIndex; ++j){
                            //LOGGER.info("\t Adding node ({}, {})", currentRow, j);
                            nodeCoordinates.add(new Coordinate(currentRow, j));
                            availableCoordinates.remove(new Coordinate(currentRow, j));
                        }
                    }


                }
            }

            //LOGGER.info("Writing maze to file ...");
            System.out.println("Writing maze to file...");
            for(int i = 0; i < maxRow; ++i) {
                String line = "";
                for(int j = 0; j < maxColumn; ++j){
                    var symbol = nodeCoordinates.contains(new Coordinate(i, j)) ? "*" : "#";
                    line += symbol;
                }
                bufferedWriter.write(line);
                if(i < maxRow - 1)
                    bufferedWriter.newLine();
            }

            //LOGGER.info("Successfully generated the custom maze!");
            //LOGGER.info("Path to file: {}", filePath);
            System.out.println("Successfully generated the custom maze!");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Coordinate findNearestNeighbour(Coordinate coordinate, List<Coordinate> coordinates) {
        int row = coordinate.rowIndex;
        int column = coordinate.columnIndex;
        var nearestNeighbour = new Coordinate(row, column);

        // Nearest neighbour from upside
        for (int i = row - 1; i >= 0; --i) {
            if (coordinates.contains(new Coordinate(i, column))) {
                nearestNeighbour.rowIndex = i;
                break;
            }
        }

        // Nearest neighbour from downside
        for (int i = row + 1; i < maxRow; ++i) {
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
        for (int j = column + 1; j < maxColumn; ++j) {
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

    private boolean isIsolated(Coordinate coordinate, List<Coordinate> coordinates){
        return !(
                coordinates.contains(new Coordinate(coordinate.rowIndex - 1, coordinate.columnIndex)) ||
                coordinates.contains(new Coordinate(coordinate.rowIndex + 1, coordinate.columnIndex)) ||
                coordinates.contains(new Coordinate(coordinate.rowIndex, coordinate.columnIndex - 1)) ||
                coordinates.contains(new Coordinate(coordinate.rowIndex, coordinate.columnIndex + 1))
        );
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("ERROR in random int generator: max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
