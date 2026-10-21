package hu.elte.szhor.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ArgumentHandler Tests")
class ArgumentHandlerTest {

    @Nested
    @DisplayName("hasHelpFlag Tests")
    class HelpFlagTests {

        @Test
        @DisplayName("Recognizes -h and --help anywhere in arguments")
        void recognizesHelpFlags() {
            assertThat(ArgumentHandler.hasHelpFlag(new String[]{"-h"})).isTrue();
            assertThat(ArgumentHandler.hasHelpFlag(new String[]{"--help"})).isTrue();
            assertThat(ArgumentHandler.hasHelpFlag(new String[]{"--input", "maze.txt", "-h"})).isTrue();
            assertThat(ArgumentHandler.hasHelpFlag(new String[]{"--output", "out.txt", "--help"})).isTrue();
        }

        @Test
        @DisplayName("Returns false when no help flag is present")
        void returnsFalseWhenNoHelpFlag() {
            assertThat(ArgumentHandler.hasHelpFlag(null)).isFalse();
            assertThat(ArgumentHandler.hasHelpFlag(new String[]{})).isFalse();
            assertThat(ArgumentHandler.hasHelpFlag(new String[]{"--input", "maze.txt", "--output", "out.csv"})).isFalse();
        }

        @Test
        @DisplayName("Help printer methods execute without exception")
        void printHelpMethodsExecuteWithoutException() {
            assertThatCode(() -> {
                ArgumentHandler.printSimulationHelp();
                ArgumentHandler.printVisualizerHelp();
                ArgumentHandler.printGeneratorHelp();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("parseSimulationConfig Tests")
    class SimulationConfigTests {

        @Test
        @DisplayName("Parses full valid arguments correctly")
        void parsesFullValidArguments() {
            final String[] args = {
                "--input", "custom_maze.txt",
                "--output", "custom_results.csv",
                "--number-of-sources", "3",
                "--chance-of-crash", "0.25",
                "--simulations", "5"
            };

            final var config = ArgumentHandler.parseSimulationConfig(args);

            assertThat(config.inputFile()).isEqualTo("custom_maze.txt");
            assertThat(config.outputFile()).isEqualTo("custom_results.csv");
            assertThat(config.numberOfSources()).isEqualTo(3);
            assertThat(config.chanceOfCrash()).isEqualTo(0.25);
            assertThat(config.simulations()).isEqualTo(5);
        }

        @Test
        @DisplayName("Applies expected default values when optional arguments are omitted")
        void appliesDefaultValues() {
            final String[] args = {"--input", "maze.txt", "--output", "results.csv"};

            final var config = ArgumentHandler.parseSimulationConfig(args);

            assertThat(config.inputFile()).isEqualTo("maze.txt");
            assertThat(config.outputFile()).isEqualTo("results.csv");
            assertThat(config.numberOfSources()).isEqualTo(1);
            assertThat(config.chanceOfCrash()).isEqualTo(0.0);
            assertThat(config.simulations()).isEqualTo(1);
        }

        @Test
        @DisplayName("Throws exception if required --input argument is missing or arguments null")
        void throwsWhenInputMissing() {
            final String[] args = {"--output", "results.csv"};

            assertThatThrownBy(() -> ArgumentHandler.parseSimulationConfig(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("--input");

            assertThatThrownBy(() -> ArgumentHandler.parseSimulationConfig(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("--input");
        }

        @Test
        @DisplayName("Throws exception if required --output argument is missing")
        void throwsWhenOutputMissing() {
            final String[] args = {"--input", "maze.txt"};

            assertThatThrownBy(() -> ArgumentHandler.parseSimulationConfig(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("--output");
        }

        @Test
        @DisplayName("Throws exception if flag value is missing")
        void throwsWhenFlagValueMissing() {
            final String[] args = {"--input", "maze.txt", "--output"};

            assertThatThrownBy(() -> ArgumentHandler.parseSimulationConfig(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing value for argument: --output");
        }

        @ParameterizedTest(name = "Invalid probability: {0}")
        @ValueSource(strings = {"-0.1", "1.1", "2.0", "not-a-number"})
        @DisplayName("Throws exception for invalid chance-of-crash values")
        void throwsForInvalidChanceOfCrash(final String invalidProb) {
            final String[] args = {"--input", "m.txt", "--output", "o.csv", "--chance-of-crash", invalidProb};

            assertThatThrownBy(() -> ArgumentHandler.parseSimulationConfig(args))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "Invalid count: {0}")
        @ValueSource(strings = {"0", "-1", "abc"})
        @DisplayName("Throws exception for non-positive number-of-sources and simulations")
        void throwsForNonPositiveCounts(final String invalidCount) {
            assertThatThrownBy(() -> ArgumentHandler.parseSimulationConfig(
                new String[]{"--input", "m.txt", "--output", "o.csv", "--number-of-sources", invalidCount}
            )).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> ArgumentHandler.parseSimulationConfig(
                new String[]{"--input", "m.txt", "--output", "o.csv", "--simulations", invalidCount}
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Throws exception on unknown argument")
        void throwsOnUnknownArgument() {
            final String[] args = {"--input", "m.txt", "--output", "o.csv", "--invalid-flag"};

            assertThatThrownBy(() -> ArgumentHandler.parseSimulationConfig(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown argument: --invalid-flag");
        }
    }

    @Nested
    @DisplayName("parseVisualizerConfig Tests")
    class VisualizerConfigTests {

        @Test
        @DisplayName("Parses full valid arguments correctly")
        void parsesFullValidVisualizerArguments() {
            final String[] args = {
                "--input", "maze.txt",
                "--number-of-sources", "2",
                "--chance-of-crash", "0.15",
                "--thread-wait", "200"
            };

            final var config = ArgumentHandler.parseVisualizerConfig(args);

            assertThat(config.inputFile()).isEqualTo("maze.txt");
            assertThat(config.numberOfSources()).isEqualTo(2);
            assertThat(config.chanceOfCrash()).isEqualTo(0.15);
            assertThat(config.threadWait()).isEqualTo(200);
        }

        @Test
        @DisplayName("Applies default visualizer values")
        void appliesDefaultVisualizerValues() {
            final String[] args = {"--input", "maze.txt"};

            final var config = ArgumentHandler.parseVisualizerConfig(args);

            assertThat(config.inputFile()).isEqualTo("maze.txt");
            assertThat(config.numberOfSources()).isEqualTo(1);
            assertThat(config.chanceOfCrash()).isEqualTo(0.0);
            assertThat(config.threadWait()).isEqualTo(100);
        }

        @Test
        @DisplayName("Throws exception when input is missing or arguments null")
        void throwsWhenVisualizerInputMissing() {
            final String[] args = {"--thread-wait", "100"};

            assertThatThrownBy(() -> ArgumentHandler.parseVisualizerConfig(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("--input");

            assertThatThrownBy(() -> ArgumentHandler.parseVisualizerConfig(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("--input");
        }

        @Test
        @DisplayName("Throws exception when thread-wait is negative")
        void throwsWhenThreadWaitNegative() {
            final String[] args = {"--input", "maze.txt", "--thread-wait", "-10"};

            assertThatThrownBy(() -> ArgumentHandler.parseVisualizerConfig(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("--thread-wait");
        }
    }

    @Nested
    @DisplayName("parseGeneratorConfig Tests")
    class GeneratorConfigTests {

        @Test
        @DisplayName("Parses full valid generator arguments")
        void parsesFullValidGeneratorArguments() {
            final String[] args = {"--output", "generated.txt", "--size", "8"};

            final var config = ArgumentHandler.parseGeneratorConfig(args);

            assertThat(config.outputFile()).isEqualTo("generated.txt");
            assertThat(config.size()).isEqualTo(8);
        }

        @Test
        @DisplayName("Applies default generator size of 4")
        void appliesDefaultGeneratorSize() {
            final String[] args = {"--output", "generated.txt"};

            final var config = ArgumentHandler.parseGeneratorConfig(args);

            assertThat(config.outputFile()).isEqualTo("generated.txt");
            assertThat(config.size()).isEqualTo(4);
        }

        @Test
        @DisplayName("Throws exception when output is missing or arguments null")
        void throwsWhenGeneratorOutputMissing() {
            final String[] args = {"--size", "6"};

            assertThatThrownBy(() -> ArgumentHandler.parseGeneratorConfig(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("--output");

            assertThatThrownBy(() -> ArgumentHandler.parseGeneratorConfig(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("--output");
        }

        @ParameterizedTest(name = "Invalid size: {0}")
        @ValueSource(strings = {"1", "0", "-2", "invalid"})
        @DisplayName("Throws exception when size is less than 2 or invalid")
        void throwsWhenSizeLessThanTwo(final String invalidSize) {
            final String[] args = {"--output", "generated.txt", "--size", invalidSize};

            assertThatThrownBy(() -> ArgumentHandler.parseGeneratorConfig(args))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
