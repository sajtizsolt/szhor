package hu.elte.szhor.core.util;

import java.util.Arrays;

public final class ArgumentHandler {

    private ArgumentHandler() {
    }

    public record SimulationConfig(
        String inputFile,
        String outputFile,
        int numberOfSources,
        double chanceOfCrash,
        int simulations
    ) {}

    public record VisualizerConfig(
        String inputFile,
        int numberOfSources,
        double chanceOfCrash,
        int threadWait
    ) {}

    public record GeneratorConfig(
        String outputFile,
        int size
    ) {}

    public static boolean hasHelpFlag(final String[] arguments) {
        if (arguments == null) return false;
        return Arrays.stream(arguments).anyMatch(arg -> "-h".equals(arg) || "--help".equals(arg));
    }

    public static SimulationConfig parseSimulationConfig(final String[] arguments) {
        final var args = arguments != null ? arguments : new String[0];
        String inputFile = null;
        String outputFile = null;
        int numberOfSources = 1;
        double chanceOfCrash = 0.0;
        int simulations = 1;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--input" -> {
                    checkHasNext(args, i, "--input");
                    inputFile = args[++i];
                }
                case "--output" -> {
                    checkHasNext(args, i, "--output");
                    outputFile = args[++i];
                }
                case "--number-of-sources" -> {
                    checkHasNext(args, i, "--number-of-sources");
                    numberOfSources = parsePositiveInt(args[++i], "--number-of-sources");
                }
                case "--chance-of-crash" -> {
                    checkHasNext(args, i, "--chance-of-crash");
                    chanceOfCrash = parseProbability(args[++i]);
                }
                case "--simulations" -> {
                    checkHasNext(args, i, "--simulations");
                    simulations = parsePositiveInt(args[++i], "--simulations");
                }
                default -> throw new IllegalArgumentException("Unknown argument: " + args[i] + ". Use --help for usage.");
            }
        }

        if (inputFile == null || inputFile.isBlank()) {
            throw new IllegalArgumentException("Missing required argument: --input <path>");
        }
        if (outputFile == null || outputFile.isBlank()) {
            throw new IllegalArgumentException("Missing required argument: --output <path>");
        }

        return new SimulationConfig(inputFile, outputFile, numberOfSources, chanceOfCrash, simulations);
    }

    public static VisualizerConfig parseVisualizerConfig(final String[] arguments) {
        final var args = arguments != null ? arguments : new String[0];
        String inputFile = null;
        int numberOfSources = 1;
        double chanceOfCrash = 0.0;
        int threadWait = 100;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--input" -> {
                    checkHasNext(args, i, "--input");
                    inputFile = args[++i];
                }
                case "--number-of-sources" -> {
                    checkHasNext(args, i, "--number-of-sources");
                    numberOfSources = parsePositiveInt(args[++i], "--number-of-sources");
                }
                case "--chance-of-crash" -> {
                    checkHasNext(args, i, "--chance-of-crash");
                    chanceOfCrash = parseProbability(args[++i]);
                }
                case "--thread-wait" -> {
                    checkHasNext(args, i, "--thread-wait");
                    threadWait = parseNonNegativeInt(args[++i], "--thread-wait");
                }
                default -> throw new IllegalArgumentException("Unknown argument: " + args[i] + ". Use --help for usage.");
            }
        }

        if (inputFile == null || inputFile.isBlank()) {
            throw new IllegalArgumentException("Missing required argument: --input <path>");
        }

        return new VisualizerConfig(inputFile, numberOfSources, chanceOfCrash, threadWait);
    }

    public static GeneratorConfig parseGeneratorConfig(final String[] arguments) {
        final var args = arguments != null ? arguments : new String[0];
        String outputFile = null;
        int size = 4;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--output" -> {
                    checkHasNext(args, i, "--output");
                    outputFile = args[++i];
                }
                case "--size" -> {
                    checkHasNext(args, i, "--size");
                    size = parsePositiveInt(args[++i], "--size");
                    if (size < 2) {
                        throw new IllegalArgumentException("--size must be at least 2, but was: " + size);
                    }
                }
                default -> throw new IllegalArgumentException("Unknown argument: " + args[i] + ". Use --help for usage.");
            }
        }

        if (outputFile == null || outputFile.isBlank()) {
            throw new IllegalArgumentException("Missing required argument: --output <path>");
        }

        return new GeneratorConfig(outputFile, size);
    }

    private static void checkHasNext(final String[] args, final int index, final String flag) {
        if (index + 1 >= args.length) {
            throw new IllegalArgumentException("Missing value for argument: " + flag);
        }
    }

    private static int parsePositiveInt(final String value, final String flag) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < 1) {
                throw new IllegalArgumentException(flag + " must be at least 1, but was: " + parsed);
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer for " + flag + ": " + value);
        }
    }

    private static int parseNonNegativeInt(final String value, final String flag) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < 0) {
                throw new IllegalArgumentException(flag + " must be >= 0, but was: " + parsed);
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer for " + flag + ": " + value);
        }
    }

    private static double parseProbability(final String value) {
        try {
            double parsed = Double.parseDouble(value);
            if (parsed < 0.0 || parsed > 1.0) {
                throw new IllegalArgumentException("Chance of crash must be between 0.0 and 1.0, but was: " + parsed);
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid decimal for --chance-of-crash: " + value);
        }
    }

    public static void printSimulationHelp() {
        System.out.println("""
            Usage: szhor-simulator --input <file> --output <file> [options]
            Options:
              --input <file>              Path to the input maze file (required)
              --output <file>             Path to the output results CSV (required)
              --number-of-sources <int>   Number of robot entry source nodes (default: 1)
              --chance-of-crash <double>  Robot crash probability between 0.0 and 1.0 (default: 0.0)
              --simulations <int>         Number of simulations to run concurrently (default: 1)
              -h, --help                  Display this help message
            """);
    }

    public static void printVisualizerHelp() {
        System.out.println("""
            Usage: szhor-visualizer --input <file> [options]
            Options:
              --input <file>              Path to the input maze file (required)
              --number-of-sources <int>   Number of robot entry source nodes (default: 1)
              --chance-of-crash <double>  Robot crash probability between 0.0 and 1.0 (default: 0.0)
              --thread-wait <int>         Delay in ms between simulation steps (default: 100)
              -h, --help                  Display this help message
            """);
    }

    public static void printGeneratorHelp() {
        System.out.println("""
            Usage: szhor-maze-generator --output <file> [options]
            Options:
              --output <file>             Path to save the generated maze (required)
              --size <int>                Grid dimension (must be >= 2, default: 4)
              -h, --help                  Display this help message
            """);
    }
}
