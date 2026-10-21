package hu.elte.szhor.core.model.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GraphBuilderTest {

    @Test
    void shouldBuildGraphFromMazeFile(@TempDir Path tempDir) throws IOException {
        var mazeFile = tempDir.resolve("test.maze");
        Files.writeString(mazeFile, """
            ***
            *#*
            ***
            """);

        var graph = GraphBuilder.fromFile(mazeFile.toString(), 2);

        // 8 nodes with '*' in a 3x3 grid (center is '#')
        assertThat(graph.getNodes()).hasSize(8);
        assertThat(graph.getSourceNodes()).hasSize(2);
    }

    @Test
    void shouldThrowExceptionWhenMazeHasDisconnectedNodes(@TempDir Path tempDir) throws IOException {
        var mazeFile = tempDir.resolve("disconnected.maze");
        Files.writeString(mazeFile, """
            ***###*
            ***###*
            """);

        assertThatThrownBy(() -> GraphBuilder.fromFile(mazeFile.toString(), 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("unreachable traversable nodes");
    }

    @Test
    void shouldThrowExceptionWhenSourceCountIsLessThanOne(@TempDir Path tempDir) throws IOException {
        var mazeFile = tempDir.resolve("test.maze");
        Files.writeString(mazeFile, """
            ***
            ***
            """);

        assertThatThrownBy(() -> GraphBuilder.fromFile(mazeFile.toString(), 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Number of source nodes must be at least 1");
    }
}
