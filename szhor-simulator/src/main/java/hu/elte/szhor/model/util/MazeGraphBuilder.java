package hu.elte.szhor.model.util;

import hu.elte.szhor.model.MazeGraph;
import hu.elte.szhor.model.Node;
import hu.elte.szhor.utils.Statistics;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MazeGraphBuilder {

    @NotNull
    public static MazeGraph fromFile(final String filename) throws IOException {
        var nodes = readNodes(filename);
        var adjacencyMatrix = createAdjacencyMatrix(nodes);
        return new MazeGraph(nodes, adjacencyMatrix, getRandomNode(nodes));
    }

    private static List<Node> readNodes(final String filename) throws IOException {
        var nodes = new LinkedList<Node>();
        try (var bufferedReader = new BufferedReader(new FileReader(filename))) {
            var rowIndex = 0;
            var line = bufferedReader.readLine();
            while (line != null) {
                for (var i = 0; i < line.length(); ++i) {
                    if (line.charAt(i) == '*') {
                        nodes.add(new Node(rowIndex, i));
                    }
                }
                line = bufferedReader.readLine();
                rowIndex++;
            }
        }
        Statistics.numberOfNodes = nodes.size();
        return nodes;
    }

    private static int[][] createAdjacencyMatrix(final List<Node> nodes) {
        var adjacencyMatrix = new int[nodes.size()][nodes.size()];
        for (var node : nodes) {
            var rightNode = Node.create(node.getX(), node.getY() + 1);
            if (nodes.contains(rightNode)) {
                var index = nodes.indexOf(rightNode);
                adjacencyMatrix[node.getId()][index] = 1;
                adjacencyMatrix[index][node.getId()] = 1;
                Statistics.numberOfEdges++;
            }
            var downNode = Node.create(node.getX() + 1, node.getY());
            if (nodes.contains(downNode)) {
                var index = nodes.indexOf(downNode);
                adjacencyMatrix[node.getId()][index] = 1;
                adjacencyMatrix[index][node.getId()] = 1;
                Statistics.numberOfEdges++;
            }
        }
        return adjacencyMatrix;
    }

    private static Node getRandomNode(final List<Node> nodes) {
        var randomNumber = ThreadLocalRandom.current().nextInt(1, nodes.size());
        return nodes.get(randomNumber);
    }
}
