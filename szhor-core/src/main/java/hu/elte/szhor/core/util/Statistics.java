package hu.elte.szhor.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Statistics {

    public static int executionTime;
    public static float chanceOfCrash;
    public static int numberOfSources;

    public static int numberOfNodes;
    public static int numberOfEdges;

    public static int numberOfRobots;
    public static int numberOfMoves;
    public static int numberOfCrashes;

    public static void saveToFile(final String filepath) throws IOException {
        if (!Files.exists(Path.of(filepath))) {
            Files.createFile(Path.of(filepath));
            Files.write(Path.of(filepath), getCsvHeaderString().getBytes(), StandardOpenOption.APPEND);
        }
        Files.write(Path.of(filepath), getCsvString().getBytes(), StandardOpenOption.APPEND);
    }

    private static String getCsvHeaderString() {
        return "executionTime,chanceOfCrash,numberOfSources,numberOfNodes,numberOfEdges,numberOfRobots,numberOfMoves,numberOfCrashes\n";
    }

    private static String getCsvString() {
        return executionTime + "," + chanceOfCrash + "," + numberOfSources + "," + numberOfNodes + "," +
                numberOfEdges + "," + numberOfRobots + "," + numberOfMoves + "," + numberOfCrashes + "\n";
    }
}
