package hu.elte.szhor.core.model.util;

import hu.elte.szhor.core.model.Graph;
import hu.elte.szhor.core.model.Node;
import hu.elte.szhor.core.util.Statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class GraphBuilder {

    public static Graph fromFile(final String filepath, final int numberOfSourceNodes) throws IOException {
        var nodes = readNodes(filepath);
        var adjacencyMatrix = createAdjacencyMatrix(nodes);
        var sourceNodes = getRandomNodes(nodes, numberOfSourceNodes);
        return new Graph(nodes, adjacencyMatrix, sourceNodes);
    }

    private static LinkedList<Node> readNodes(final String filename) throws IOException {
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

    private static int[][] createAdjacencyMatrix(final LinkedList<Node> nodes) {
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

    private static LinkedList<Node> getRandomNodes(final LinkedList<Node> nodes, final int numberOfNodes) {
        var randomNodes = new LinkedList<Node>();
        var randomGenerator = new Random();
        var i = 0;
        while (i < numberOfNodes) {
            var randomNode = nodes.get(randomGenerator.nextInt(nodes.size()));
            if (!randomNodes.contains(randomNode)) {
                randomNodes.add(randomNode);
                ++i;
            }
        }
        return randomNodes;
    }
}
