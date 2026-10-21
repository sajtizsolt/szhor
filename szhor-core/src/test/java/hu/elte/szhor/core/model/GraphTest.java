package hu.elte.szhor.core.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GraphTest {

    @Test
    void shouldReportNeighboursCorrectly() {
        var n0 = new Node(0, 0, 0);
        var n1 = new Node(1, 0, 1);
        var nodes = List.of(n0, n1);

        var matrix = new int[2][2];
        matrix[0][1] = 1;
        matrix[1][0] = 1;

        var sources = List.of(n0);

        var graph = new Graph(nodes, matrix, sources);

        var neighbours = graph.getNeighbours(n0);
        assertThat(neighbours).containsExactly(n1);
        assertThat(graph.getSourceNodes()).containsExactly(n0);
        assertThat(graph.isSourceNode(n0)).isTrue();
        assertThat(graph.isSourceNode(n1)).isFalse();
    }

    @Test
    void shouldCheckIfEveryNonSourceNodeIsOccupied() {
        var n0 = new Node(0, 0, 0);
        var n1 = new Node(1, 0, 1);
        var nodes = List.of(n0, n1);

        var matrix = new int[2][2];
        var sources = List.of(n0);

        var graph = new Graph(nodes, matrix, sources);

        assertThat(graph.isEveryNodeOccupied()).isFalse();

        var robot = new Robot(1, graph, n0, 0);
        n1.setSettledRobot(robot);

        assertThat(graph.isEveryNodeOccupied()).isTrue();
    }

    @Test
    void shouldValidateConnectivityFromSources() {
        var n0 = new Node(0, 0, 0);
        var n1 = new Node(1, 0, 1);
        var n2 = new Node(2, 5, 5); // Disconnected component
        var nodes = List.of(n0, n1, n2);

        var matrix = new int[3][3];
        matrix[0][1] = 1;
        matrix[1][0] = 1;

        var graphWithDisconnected = new Graph(nodes, matrix, List.of(n0));
        assertThat(graphWithDisconnected.isConnectedFromSources()).isFalse();

        var connectedNodes = List.of(n0, n1);
        var connectedMatrix = new int[2][2];
        connectedMatrix[0][1] = 1;
        connectedMatrix[1][0] = 1;

        var connectedGraph = new Graph(connectedNodes, connectedMatrix, List.of(n0));
        assertThat(connectedGraph.isConnectedFromSources()).isTrue();
    }

    @Test
    void shouldConstructGraphFromAdjacencyListDirectly() {
        var n0 = new Node(0, 0, 0);
        var n1 = new Node(1, 0, 1);
        var n2 = new Node(2, 0, 2);

        var adjacencyList = Map.of(
            n0, List.of(n1),
            n1, List.of(n0, n2),
            n2, List.of(n1)
        );

        var graph = new Graph(List.of(n0, n1, n2), adjacencyList, List.of(n0));

        assertThat(graph.getNumberOfEdges()).isEqualTo(2);
        assertThat(graph.getNeighbours(n1)).containsExactlyInAnyOrder(n0, n2);
        assertThat(graph.isConnectedFromSources()).isTrue();
    }
}
