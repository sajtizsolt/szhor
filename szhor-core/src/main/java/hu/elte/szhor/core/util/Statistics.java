package hu.elte.szhor.core.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Statistics {

    public static int executionTime = 0;
    public static float chanceOfCrash = 0;
    public static int numberOfSources = 0;

    public static int numberOfNodes = 0;
    public static int numberOfEdges = 0;

    public static int numberOfRobots = 0;
    public static int numberOfMoves = 0;
    public static int numberOfCrashes = 0;

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
