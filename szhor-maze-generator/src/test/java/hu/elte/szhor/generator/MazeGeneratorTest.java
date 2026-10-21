package hu.elte.szhor.generator;

import hu.elte.szhor.core.model.util.GraphBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MazeGeneratorTest {

    @Test
    void shouldGenerateMazeWithCorrectDimensionsAndBorders() {
        int size = 5;
        var maze = MazeGenerator.generate(size);

        assertThat(maze).hasDimensions(size, size);

        // Verify border is filled with '*'
        for (int i = 0; i < size; i++) {
            assertThat(maze[0][i]).isEqualTo("*");
            assertThat(maze[size - 1][i]).isEqualTo("*");
            assertThat(maze[i][0]).isEqualTo("*");
            assertThat(maze[i][size - 1]).isEqualTo("*");
        }
    }

    @Test
    void shouldThrowExceptionWhenSizeIsLessThanTwo() {
        assertThatThrownBy(() -> MazeGenerator.generate(1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Maze size must be at least 2");

        assertThatThrownBy(() -> MazeGenerator.generate(0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldSaveMazeToFile(@TempDir Path tempDir) throws IOException {
        var maze = MazeGenerator.generate(3);
        var outputFile = tempDir.resolve("generated.maze");

        MazeGenerator.saveToFile(outputFile.toString(), maze);

        assertThat(Files.exists(outputFile)).isTrue();
        var content = Files.readString(outputFile);
        assertThat(content).hasLineCount(3);
    }

    @Test
    void shouldGenerateMazeThatFormsValidConnectedGraph(@TempDir Path tempDir) throws IOException {
        for (int size : new int[]{4, 6, 8, 12}) {
            var maze = MazeGenerator.generate(size);
            var outputFile = tempDir.resolve("maze_" + size + ".maze");

            MazeGenerator.saveToFile(outputFile.toString(), maze);

            var graph = GraphBuilder.fromFile(outputFile.toString(), 2);
            assertThat(graph.getNodes().size()).isGreaterThanOrEqualTo(4 * size - 4);
            assertThat(graph.getSourceNodes()).hasSize(2);
            assertThat(graph.isConnectedFromSources()).isTrue();
        }
    }
}
