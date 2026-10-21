package hu.elte.szhor.visualizer.view;

import hu.elte.szhor.core.model.Node;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.awt.Color;

public class MazeGraphDisplay {

    private static final String STYLESHEET = """
        node {
            shape: box;
            size: 20px;
            text-alignment: under;
            text-size: 12px;
            fill-mode: gradient-diagonal1;
            stroke-mode: dashes;
        }
        edge {
            shape: blob;
            fill-color: rgb(75, 75, 75);
        }
        """;

    private final Graph graph;

    public MazeGraphDisplay() {
        this.graph = new SingleGraph("Visualization of Fast Uniform Dispersion of a Crash-prone Swarm");
        System.setProperty("org.graphstream.ui", "swing");
        this.graph.setAttribute("ui.quality");
        this.graph.setAttribute("ui.antialias");
        this.graph.setAttribute("ui.stylesheet", STYLESHEET);
    }

    public void display() {
        this.graph.display();
    }

    public void addNode(final Node node) {
        this.graph.addNode(node.getIdAsString());
        this.setColor(node, Color.BLACK);
        this.setLabel(node);
    }

    public void addEdge(final String id, final Node from, final Node to) {
        this.graph.addEdge(id, from.getIdAsString(), to.getIdAsString());
    }

    public void setColor(final Node node, final Color color) {
        var displayNode = this.graph.getNode(node.getIdAsString());
        if (displayNode != null) {
            displayNode.setAttribute("ui.style", "fill-color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "), rgb(255,255,255);");
        }
    }

    public void setLabel(final Node node) {
        var displayNode = this.graph.getNode(node.getIdAsString());
        if (displayNode != null) {
            displayNode.setAttribute("ui.label", node.toString());
        }
    }

    public void refreshNode(final Node node) {
        this.refreshNode(node, false);
    }

    public void refreshNode(final Node node, final boolean isSource) {
        if (node.getSettledRobot() != null && node.getMobileRobot() == null) {
            this.setColor(node, Color.GREEN);
        } else if (node.getSettledRobot() != null && node.getMobileRobot() != null) {
            this.setColor(node, Color.RED);
        } else if (node.getSettledRobot() == null && node.getMobileRobot() != null) {
            this.setColor(node, Color.ORANGE);
        } else {
            this.setColor(node, isSource ? Color.BLUE : Color.BLACK);
        }
        this.setLabel(node);
    }

    public Graph getGraph() {
        return this.graph;
    }
}
