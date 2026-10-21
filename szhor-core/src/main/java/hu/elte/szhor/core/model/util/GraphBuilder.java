package hu.elte.szhor.core.model.util;

import hu.elte.szhor.core.model.Graph;
import hu.elte.szhor.core.model.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GraphBuilder {

    private GraphBuilder() {
    }

    public static Graph fromFile(final String filepath, final int numberOfSourceNodes) throws IOException {
        var nodes = readNodes(filepath);
        var adjacencyList = createAdjacencyList(nodes);
        var sourceNodes = getRandomNodes(nodes, numberOfSourceNodes);
        var graph = new Graph(nodes, adjacencyList, sourceNodes);
        if (!graph.isConnectedFromSources()) {
            throw new IllegalArgumentException(
                "Input maze contains unreachable traversable nodes disconnected from all source nodes."
            );
        }
        return graph;
    }

    private static List<Node> readNodes(final String filename) throws IOException {
        var nodes = new ArrayList<Node>();
        try (var bufferedReader = Files.newBufferedReader(Path.of(filename), StandardCharsets.UTF_8)) {
            var rowIndex = 0;
            var nodeId = 0;
            var line = bufferedReader.readLine();
            while (line != null) {
                for (var i = 0; i < line.length(); ++i) {
                    if (line.charAt(i) == '*') {
                        nodes.add(new Node(nodeId++, rowIndex, i));
                    }
                }
                line = bufferedReader.readLine();
                rowIndex++;
            }
        }
        return nodes;
    }

    private static Map<Node, List<Node>> createAdjacencyList(final List<Node> nodes) {
        var adjacencyList = new HashMap<Node, List<Node>>(nodes.size());
        var coordinateMap = new HashMap<Long, Node>(nodes.size());
        for (var node : nodes) {
            coordinateMap.put((((long) node.getX()) << 32) | (node.getY() & 0xFFFFFFFFL), node);
            adjacencyList.put(node, new ArrayList<>(4));
        }

        for (var node : nodes) {
            var rightKey = (((long) node.getX()) << 32) | ((node.getY() + 1) & 0xFFFFFFFFL);
            var rightNode = coordinateMap.get(rightKey);
            if (rightNode != null) {
                adjacencyList.get(node).add(rightNode);
                adjacencyList.get(rightNode).add(node);
            }

            var downKey = (((long) (node.getX() + 1)) << 32) | (node.getY() & 0xFFFFFFFFL);
            var downNode = coordinateMap.get(downKey);
            if (downNode != null) {
                adjacencyList.get(node).add(downNode);
                adjacencyList.get(downNode).add(node);
            }
        }
        return adjacencyList;
    }

    private static List<Node> getRandomNodes(final List<Node> nodes, final int numberOfNodes) {
        if (numberOfNodes < 1) {
            throw new IllegalArgumentException(
                "Number of source nodes must be at least 1, but was: " + numberOfNodes
            );
        }
        if (numberOfNodes > nodes.size()) {
            throw new IllegalArgumentException(
                "Number of source nodes (" + numberOfNodes + ") cannot exceed total nodes (" + nodes.size() + ")"
            );
        }
        var shuffled = new ArrayList<>(nodes);
        Collections.shuffle(shuffled);
        return new ArrayList<>(shuffled.subList(0, numberOfNodes));
    }
}
