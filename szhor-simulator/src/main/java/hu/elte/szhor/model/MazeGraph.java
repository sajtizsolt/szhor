package hu.elte.szhor.model;

import java.util.LinkedList;
import java.util.List;

public class MazeGraph {

    private final Node sourceNode;
    private final List<Node> nodes;
    private final int[][] adjacencyMatrix;

    public MazeGraph(final List<Node> nodes, final int[][] adjacencyMatrix, final Node sourceNode) {
        this.nodes = nodes;
        this.adjacencyMatrix = adjacencyMatrix;
        this.sourceNode = sourceNode;
    }

    public Node getSourceNode() {
        return this.sourceNode;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public List<Node> getNeighbours(final Node node) {
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
            if (!node.equals(this.sourceNode) && node.getSettledRobot() == null) {
                return false;
            }
        }
        return true;
    }
}
