package hu.elte.szhor.simulator;

import hu.elte.szhor.core.model.util.GraphBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class SimulationTest {

    @Test
    void shouldRunSimulationToCompletion(@TempDir Path tempDir) throws IOException {
        var mazeFile = tempDir.resolve("maze.maze");
        Files.writeString(mazeFile, """
            ***
            *#*
            ***
            """);

        var graph = GraphBuilder.fromFile(mazeFile.toString(), 1);
        var simulation = new Simulation(graph, 0.0);
        var stats = simulation.run();

        assertThat(stats.getExecutionTime()).isGreaterThan(0);
        assertThat(stats.getNumberOfNodes()).isEqualTo(8);
        assertThat(stats.getNumberOfSources()).isEqualTo(1);
        assertThat(stats.getNumberOfCrashes()).isEqualTo(0);
        assertThat(stats.getNumberOfMoves()).isGreaterThan(0);
        assertThat(graph.isEveryNodeOccupied()).isTrue();
    }

    @Test
    void shouldRunMultipleSimulationsSequentiallyWithoutStaticStateCollision(@TempDir Path tempDir) throws IOException {
        var mazeFile = tempDir.resolve("maze.maze");
        Files.writeString(mazeFile, """
            ***
            *#*
            ***
            """);

        var graph1 = GraphBuilder.fromFile(mazeFile.toString(), 1);
        var sim1 = new Simulation(graph1, 0.0);
        var stats1 = sim1.run();

        var graph2 = GraphBuilder.fromFile(mazeFile.toString(), 1);
        var sim2 = new Simulation(graph2, 0.0);
        var stats2 = sim2.run();

        assertThat(stats1.getExecutionTime()).isGreaterThan(0);
        assertThat(stats2.getExecutionTime()).isGreaterThan(0);
        assertThat(graph1.isEveryNodeOccupied()).isTrue();
        assertThat(graph2.isEveryNodeOccupied()).isTrue();
    }

    @Test
    void shouldRunMultipleSimulationsConcurrentlyWithVirtualThreads(@TempDir Path tempDir) throws IOException, InterruptedException, ExecutionException {
        var mazeFile = tempDir.resolve("maze.maze");
        Files.writeString(mazeFile, """
            ***
            *#*
            ***
            """);

        int concurrency = 10;
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var tasks = IntStream.range(0, concurrency)
                .mapToObj(i -> (Callable<hu.elte.szhor.core.util.Statistics>) () -> {
                    var graph = GraphBuilder.fromFile(mazeFile.toString(), 1);
                    return new Simulation(graph, 0.1).run();
                })
                .toList();

            var futures = executor.invokeAll(tasks);
            for (var future : futures) {
                var stats = future.get();
                assertThat(stats.getExecutionTime()).isGreaterThan(0);
                assertThat(stats.getNumberOfNodes()).isEqualTo(8);
                assertThat(stats.getNumberOfSources()).isEqualTo(1);
            }
        }
    }

    @Test
    void shouldAchieveFullOccupancyWithCrashRecovery(@TempDir Path tempDir) throws IOException {
        var mazeFile = tempDir.resolve("maze.maze");
        Files.writeString(mazeFile, """
            ***
            *#*
            ***
            """);

        // 35% chance of crash guarantees crashes occur while swarm continues to replenish
        var graph = GraphBuilder.fromFile(mazeFile.toString(), 1);
        var simulation = new Simulation(graph, 0.35);
        var stats = simulation.run();

        assertThat(stats.getNumberOfCrashes()).isGreaterThan(0);
        assertThat(stats.getNumberOfRobots()).isGreaterThanOrEqualTo(stats.getNumberOfNodes());
        assertThat(graph.isEveryNodeOccupied()).isTrue();
    }

    @Test
    void shouldRunSimulationOnCanonicalBenchmarkMaze() throws IOException {
        var resource = getClass().getResource("/figure1.maze");
        assertThat(resource).isNotNull();

        var graph = GraphBuilder.fromFile(resource.getPath(), 2);
        var simulation = new Simulation(graph, 0.05);
        var stats = simulation.run();

        assertThat(stats.getExecutionTime()).isGreaterThan(0);
        assertThat(stats.getNumberOfNodes()).isGreaterThan(20);
        assertThat(stats.getNumberOfSources()).isEqualTo(2);
        assertThat(graph.isEveryNodeOccupied()).isTrue();
    }
}
