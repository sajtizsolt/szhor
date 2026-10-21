package hu.elte.szhor.core.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Statistics {

    private int executionTime;
    private double chanceOfCrash;
    private int numberOfSources;
    private int numberOfNodes;
    private int numberOfEdges;
    private int numberOfRobots;
    private int numberOfMoves;
    private int numberOfCrashes;

    public Statistics() {
    }

    public void incrementMoves() {
        this.numberOfMoves++;
    }

    public void incrementCrashes() {
        this.numberOfCrashes++;
    }

    public void incrementRobots() {
        this.numberOfRobots++;
    }

    public void incrementExecutionTime() {
        this.executionTime++;
    }

    public int getExecutionTime() {
        return this.executionTime;
    }

    public void setExecutionTime(final int executionTime) {
        this.executionTime = executionTime;
    }

    public double getChanceOfCrash() {
        return this.chanceOfCrash;
    }

    public void setChanceOfCrash(final double chanceOfCrash) {
        this.chanceOfCrash = chanceOfCrash;
    }

    public int getNumberOfSources() {
        return this.numberOfSources;
    }

    public void setNumberOfSources(final int numberOfSources) {
        this.numberOfSources = numberOfSources;
    }

    public int getNumberOfNodes() {
        return this.numberOfNodes;
    }

    public void setNumberOfNodes(final int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public int getNumberOfEdges() {
        return this.numberOfEdges;
    }

    public void setNumberOfEdges(final int numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }

    public int getNumberOfRobots() {
        return this.numberOfRobots;
    }

    public void setNumberOfRobots(final int numberOfRobots) {
        this.numberOfRobots = numberOfRobots;
    }

    public int getNumberOfMoves() {
        return this.numberOfMoves;
    }

    public void setNumberOfMoves(final int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    public int getNumberOfCrashes() {
        return this.numberOfCrashes;
    }

    public void setNumberOfCrashes(final int numberOfCrashes) {
        this.numberOfCrashes = numberOfCrashes;
    }

    public void saveToFile(final String filepath) throws IOException {
        saveAllToFile(filepath, List.of(this));
    }

    public static void saveAllToFile(final String filepath, final List<Statistics> statisticsList) throws IOException {
        if (statisticsList == null || statisticsList.isEmpty()) {
            return;
        }
        var path = Path.of(filepath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        var writeHeader = !Files.exists(path) || Files.size(path) == 0;
        try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (writeHeader) {
                writer.write(getCsvHeaderString());
            }
            for (var stats : statisticsList) {
                writer.write(stats.getCsvString());
            }
        }
    }

    private static String getCsvHeaderString() {
        return "executionTime,chanceOfCrash,numberOfSources,numberOfNodes,numberOfEdges,numberOfRobots,numberOfMoves,numberOfCrashes\n";
    }

    private String getCsvString() {
        return this.executionTime + "," + this.chanceOfCrash + "," + this.numberOfSources + ","
                + this.numberOfNodes + "," + this.numberOfEdges + "," + this.numberOfRobots + ","
                + this.numberOfMoves + "," + this.numberOfCrashes + "\n";
    }
}
