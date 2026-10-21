package hu.elte.szhor.simulator;

import hu.elte.szhor.core.model.util.GraphBuilder;
import hu.elte.szhor.core.util.ArgumentHandler;
import hu.elte.szhor.core.util.Statistics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Application {

    public static void main(final String[] arguments) throws IOException {
        if (ArgumentHandler.hasHelpFlag(arguments)) {
            ArgumentHandler.printSimulationHelp();
            return;
        }

        final var config = ArgumentHandler.parseSimulationConfig(arguments);

        if (config.simulations() <= 1) {
            final var graph = GraphBuilder.fromFile(config.inputFile(), config.numberOfSources());
            final var simulation = new Simulation(graph, config.chanceOfCrash());
            final var stats = simulation.run();
            stats.saveToFile(config.outputFile());
            return;
        }

        // Run batch simulations concurrently using Java 25 Virtual Threads
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final List<Callable<Statistics>> tasks = IntStream.range(0, config.simulations())
                .mapToObj(i -> (Callable<Statistics>) () -> {
                    var graph = GraphBuilder.fromFile(config.inputFile(), config.numberOfSources());
                    return new Simulation(graph, config.chanceOfCrash()).run();
                })
                .toList();

            var futures = executor.invokeAll(tasks);
            var results = new ArrayList<Statistics>(config.simulations());
            for (var future : futures) {
                results.add(future.get());
            }
            Statistics.saveAllToFile(config.outputFile(), results);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Simulation execution was interrupted", e);
        } catch (ExecutionException e) {
            var cause = e.getCause() != null ? e.getCause() : e;
            throw new RuntimeException("Simulation execution failed: " + cause.getMessage(), cause);
        }
    }
}
