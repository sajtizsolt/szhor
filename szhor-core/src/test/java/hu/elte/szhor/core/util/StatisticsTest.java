package hu.elte.szhor.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StatisticsTest {

    @Test
    void shouldTrackIncrementsAndFieldValues() {
        var stats = new Statistics();
        stats.setChanceOfCrash(0.25);
        stats.setNumberOfSources(2);
        stats.setNumberOfNodes(10);
        stats.setNumberOfEdges(15);
        stats.setExecutionTime(5);
        stats.setNumberOfRobots(3);
        stats.setNumberOfMoves(8);
        stats.setNumberOfCrashes(1);

        stats.incrementExecutionTime();
        stats.incrementRobots();
        stats.incrementMoves();
        stats.incrementCrashes();

        assertThat(stats.getExecutionTime()).isEqualTo(6);
        assertThat(stats.getNumberOfRobots()).isEqualTo(4);
        assertThat(stats.getNumberOfMoves()).isEqualTo(9);
        assertThat(stats.getNumberOfCrashes()).isEqualTo(2);
        assertThat(stats.getChanceOfCrash()).isEqualTo(0.25);
        assertThat(stats.getNumberOfSources()).isEqualTo(2);
        assertThat(stats.getNumberOfNodes()).isEqualTo(10);
        assertThat(stats.getNumberOfEdges()).isEqualTo(15);
    }

    @Test
    void shouldSaveSingleStatisticsToFile(@TempDir Path tempDir) throws IOException {
        var stats = new Statistics();
        stats.setChanceOfCrash(0.0);
        stats.setNumberOfSources(1);
        stats.setNumberOfNodes(4);
        stats.setNumberOfEdges(4);
        stats.setNumberOfRobots(2);
        stats.setNumberOfMoves(10);
        stats.setNumberOfCrashes(0);
        stats.setExecutionTime(5);

        var file = tempDir.resolve("stats.csv");
        stats.saveToFile(file.toString());

        assertThat(Files.exists(file)).isTrue();
        var lines = Files.readAllLines(file);
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0)).isEqualTo("executionTime,chanceOfCrash,numberOfSources,numberOfNodes,numberOfEdges,numberOfRobots,numberOfMoves,numberOfCrashes");
        assertThat(lines.get(1)).isEqualTo("5,0.0,1,4,4,2,10,0");
    }

    @Test
    void shouldBatchSaveStatisticsToFile(@TempDir Path tempDir) throws IOException {
        var s1 = new Statistics();
        s1.setExecutionTime(1);
        s1.setNumberOfMoves(5);

        var s2 = new Statistics();
        s2.setExecutionTime(2);
        s2.setNumberOfMoves(12);

        var file = tempDir.resolve("batch-stats.csv");
        Statistics.saveAllToFile(file.toString(), List.of(s1, s2));

        var lines = Files.readAllLines(file);
        assertThat(lines).hasSize(3); // header + 2 rows
        assertThat(lines.get(1)).startsWith("1,0.0,0,0,0,0,5,0");
        assertThat(lines.get(2)).startsWith("2,0.0,0,0,0,0,12,0");
    }

    @Test
    void shouldWriteHeaderWhenTargetFilePreExistsEmpty(@TempDir Path tempDir) throws IOException {
        var emptyFile = tempDir.resolve("empty.csv");
        Files.createFile(emptyFile);
        assertThat(Files.size(emptyFile)).isEqualTo(0);

        var s = new Statistics();
        s.setExecutionTime(10);
        Statistics.saveAllToFile(emptyFile.toString(), List.of(s));

        var lines = Files.readAllLines(emptyFile);
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0)).startsWith("executionTime,chanceOfCrash");
        assertThat(lines.get(1)).startsWith("10,0.0");
    }
}
