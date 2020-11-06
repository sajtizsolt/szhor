package hu.elte.szhor.utils;

import hu.elte.szhor.model.Node;
import org.graphstream.graph.Graph;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GraphModel {

    private int size;

    private final Graph view;
    private final Node source;
    private final int[][] adjacencyMatrix;
    private final List<Node> nodes;

    public GraphModel(Graph view, final String filename) {
        this.view = view;
        this.nodes = new ArrayList<>();
        this.readNodes(filename);
        this.adjacencyMatrix = new int[this.size][this.size];
        this.addEdges();
        this.source = this.getRandomNode();
    }

    public Node getSource() {
        return this.source;
    }

    public List<Node> getNodes() {
        return this.nodes;
    }

    public List<Node> getNeighbours(final Node node) {
        var neighbours = new ArrayList<Node>();

        for (var i = 0; i < size; ++i) {
            if (this.adjacencyMatrix[node.getId()][i] == 1) {
                neighbours.add(nodes.get(i));
            }
        }

        return neighbours;
    }

    private Node getRandomNode() {
        return this.nodes.get(ThreadLocalRandom.current().nextInt(0, this.size));
    }

    public void refresh(final Node node) {
        if (node.equals(source)) {
            this.view.getNode("" + node.getId()).setAttribute("ui.style", "fill-color: rgb(0,0,255); text-alignment: under;");
            this.view.getNode(node.getId()).setAttribute("ui.label", node.getId() + "(" + node.getSettledRobot() + "," + node.getMobileRobot() + ")");
        }
        else if (node.getMobileRobot() == null && node.getSettledRobot() == null) {
            this.view.getNode("" + node.getId()).setAttribute("ui.style", "fill-color: rgb(0,0,0); text-alignment: under;");
            this.view.getNode(node.getId()).setAttribute("ui.label", node.getId() + "(" + node.getSettledRobot() + "," + node.getMobileRobot() + ")");
        }
        else if (node.getMobileRobot() == null && node.getSettledRobot() != null) {
            this.view.getNode("" + node.getId()).setAttribute("ui.style", "fill-color: rgb(0,255,0); text-alignment: under;");
            this.view.getNode(node.getId()).setAttribute("ui.label", node.getId() + "(" + node.getSettledRobot() + "," + node.getMobileRobot() + ")");
        }
        else if (node.getMobileRobot() != null && node.getSettledRobot() != null) {
            this.view.getNode("" + node.getId()).setAttribute("ui.style", "fill-color: rgb(255,0,0); text-alignment: under;");
            this.view.getNode(node.getId()).setAttribute("ui.label", node.getId() + "(" + node.getSettledRobot() + "," + node.getMobileRobot() + ")");
        }
    }

    public void refreshAll() {
        for (var node : this.nodes) {
            this.refresh(node);
        }
    }

    public boolean isGraphFull() {
        this.refreshAll();
        for (var node : nodes) {
            if (!node.equals(this.source) && node.getSettledRobot() == null) {
                return false;
            }
        }
        return true;
    }

    private void readNodes(final String filename) {
        try (var bufferedReader = new BufferedReader(new FileReader(filename))) {
            var rowIndex = 0;
            var line = bufferedReader.readLine();
            while (line != null) {
                for (var i = 0; i < line.length(); ++i) {
                    if (line.charAt(i) == '*') {
                        this.nodes.add(new Node(rowIndex, i));
                        ++this.size;
                    }
                }
                line = bufferedReader.readLine();
                rowIndex++;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void addEdges() {
        for (var node : this.nodes) {
            var rightNode = Node.create(node.getX(), node.getY() + 1);
            if (this.nodes.contains(rightNode)) {
                var index = this.nodes.indexOf(rightNode);
                this.adjacencyMatrix[node.getId()][index] = 1;
                this.adjacencyMatrix[index][node.getId()] = 1;
            }
            var downNode = Node.create(node.getX() + 1, node.getY());
            if (this.nodes.contains(downNode)) {
                var index = this.nodes.indexOf(downNode);
                this.adjacencyMatrix[node.getId()][index] = 1;
                this.adjacencyMatrix[index][node.getId()] = 1;
            }
        }
    }
}
