package hu.elte.szhor.util;

import hu.elte.szhor.model.Edge;
import hu.elte.szhor.model.Vertex;
import org.jgrapht.Graph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GraphBuilderHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphBuilderHelper.class);

    public static Graph<Vertex, Edge> buildEmptySimpleGraph() {
        return GraphTypeBuilder
                .<Vertex, Edge> undirected().allowingMultipleEdges(false)
                .allowingSelfLoops(false).edgeClass(Edge.class).weighted(false).buildGraph();
    }



    public static Vertex fillGraph(Graph<Vertex, Edge> graph, final int origin, final int bound) {
        final var vertexCount = ThreadLocalRandom.current().nextInt(origin, bound);
        LOGGER.info("Graph size: {}", vertexCount);
        var vertices = new ArrayList<Vertex>();
        for (var i = 0; i < vertexCount; ++i) {
            var vertex = new Vertex(graph, UUID.randomUUID().toString());
            vertices.add(vertex);
            graph.addVertex(vertex);
        }

        final var edgeCount = vertexCount * (vertexCount - 1) / 2;
        for (var i = 0; i < edgeCount; ++i) {
            var success = false;
            while (!success) {
                var vertex1 = ThreadLocalRandom.current().nextInt(0, vertexCount);
                var vertex2 = ThreadLocalRandom.current().nextInt(0, vertexCount);
                if (vertex1 != vertex2 && !graph.containsEdge(vertices.get(vertex1), vertices.get(vertex2))) {
                    var edge = graph.addEdge(vertices.get(vertex1), vertices.get(vertex2));
                    success = true;
                    LOGGER.info("Edge added between {} and {}.", edge.getSourceVertex(), edge.getTargetVertex());
                }
            }
        }

        var source = vertices.get(ThreadLocalRandom.current().nextInt(0, vertexCount));
        LOGGER.info("Graph filled. Source vertex: {}", source);
        return source;
    }

    public static Graph<Vertex, Edge> getGraph(final int size) {
        var graph = buildEmptySimpleGraph();
        var matrix = new Vertex[size][size];

        for (var i = 0; i < size; ++i) {
            for (var j = 0; j < size; ++j) {
                matrix[i][j] = new Vertex(graph, UUID.randomUUID().toString());
                graph.addVertex(matrix[i][j]);
            }
        }

        for (var i = 0; i < size; ++i) {
            for (var j = 0; j < size; ++j) {
                if (i < size - 1) {
                    graph.addEdge(matrix[i][j], matrix[i + 1][j]);
                }
                if (j < size - 1) {
                    graph.addEdge(matrix[i][j], matrix[i][j + 1]);
                }
            }
        }

        return graph;
    }
}
