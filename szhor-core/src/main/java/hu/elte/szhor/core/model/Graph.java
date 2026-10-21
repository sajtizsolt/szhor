package hu.elte.szhor.core.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

    private final List<Node> sourceNodes;
    private final Set<Node> sourceNodeSet;
    private final List<Node> nodes;
    private final List<Node> targetNodes;
    private final Map<Node, List<Node>> adjacencyList;
    private final int numberOfEdges;
    private int firstUnoccupiedTargetIndex = 0;

    public Graph(final List<Node> nodes, final Map<Node, List<Node>> adjacencyList, final List<Node> sourceNodes) {
        this.nodes = new ArrayList<>(nodes);
        this.sourceNodes = new ArrayList<>(sourceNodes);
        this.sourceNodeSet = new HashSet<>(sourceNodes);
        this.targetNodes = this.nodes.stream()
                .filter(node -> !this.sourceNodeSet.contains(node))
                .toList();

        this.adjacencyList = new HashMap<>(nodes.size());
        for (var entry : adjacencyList.entrySet()) {
            this.adjacencyList.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        var edgeCount = 0;
        for (var neighbours : this.adjacencyList.values()) {
            edgeCount += neighbours.size();
        }
        this.numberOfEdges = edgeCount / 2;
    }

    public Graph(final List<Node> nodes, final int[][] adjacencyMatrix, final List<Node> sourceNodes) {
        this(nodes, matrixToAdjacencyList(nodes, adjacencyMatrix), sourceNodes);
    }

    private static Map<Node, List<Node>> matrixToAdjacencyList(final List<Node> nodes, final int[][] matrix) {
        var map = new HashMap<Node, List<Node>>(nodes.size());
        for (var i = 0; i < nodes.size(); ++i) {
            var neighbours = new ArrayList<Node>(4);
            for (var j = 0; j < nodes.size(); ++j) {
                if (matrix[i][j] == 1) {
                    neighbours.add(nodes.get(j));
                }
            }
            map.put(nodes.get(i), neighbours);
        }
        return map;
    }

    public List<Node> getSourceNodes() {
        return Collections.unmodifiableList(this.sourceNodes);
    }

    public boolean isSourceNode(final Node node) {
        return this.sourceNodeSet.contains(node);
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    public List<Node> getTargetNodes() {
        return Collections.unmodifiableList(this.targetNodes);
    }

    public List<Node> getNeighbours(final Node node) {
        var neighbours = this.adjacencyList.get(node);
        return neighbours == null ? List.of() : Collections.unmodifiableList(neighbours);
    }

    public int getNumberOfEdges() {
        return this.numberOfEdges;
    }

    public boolean isConnectedFromSources() {
        if (this.nodes.isEmpty()) {
            return true;
        }
        if (this.sourceNodes.isEmpty()) {
            return false;
        }

        var visited = new HashSet<Node>();
        var queue = new ArrayDeque<Node>(this.sourceNodes);
        visited.addAll(this.sourceNodes);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            for (var neighbour : getNeighbours(current)) {
                if (visited.add(neighbour)) {
                    queue.add(neighbour);
                }
            }
        }
        return visited.size() == this.nodes.size();
    }

    public boolean isEveryNodeOccupied() {
        while (this.firstUnoccupiedTargetIndex < this.targetNodes.size()) {
            if (this.targetNodes.get(this.firstUnoccupiedTargetIndex).getSettledRobot() == null) {
                return false;
            }
            this.firstUnoccupiedTargetIndex++;
        }
        return true;
    }
}
