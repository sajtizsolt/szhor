package hu.elte.szhor.core.model;

import java.util.LinkedList;

public class Graph {

    private final LinkedList<Node> sourceNodes;
    private final LinkedList<Node> nodes;
    private final int[][] adjacencyMatrix;

    public Graph(final LinkedList<Node> nodes, final int[][] adjacencyMatrix, final LinkedList<Node> sourceNodes) {
        this.nodes = nodes;
        this.adjacencyMatrix = adjacencyMatrix;
        this.sourceNodes = sourceNodes;
    }

    public LinkedList<Node> getSourceNodes() {
        return this.sourceNodes;
    }

    public LinkedList<Node> getNodes() {
        return this.nodes;
    }

    public LinkedList<Node> getNeighbours(final Node node) {
        var neighbours = new LinkedList<Node>();
        for (var i = 0; i < this.nodes.size(); ++i) {
            if (this.adjacencyMatrix[node.getId()][i] == 1) {
                neighbours.add(this.nodes.get(i));
            }
        }
        return neighbours;
    }

    public boolean isEveryNodeOccupied() {
        for (var node : this.nodes) {
            if (this.sourceNodes.contains(node)) {
                continue;
            }
            if (node.getSettledRobot() == null) {
                return false;
            }
        }
        return true;
    }
}
