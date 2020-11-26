package hu.elte.szhor.visualizer.view;

import hu.elte.szhor.core.model.Node;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.awt.*;

public class MazeGraphDisplay {

    private final Graph graph;

    public MazeGraphDisplay() {
        this.graph = new SingleGraph("Visualization of Fast Uniform Dispersion of a Crash-prone Swarm");
        System.setProperty("org.graphstream.ui", "swing");
        this.graph.setAttribute("ui.quality");
        this.graph.setAttribute("ui.antialias");
    }

    public void display() {
        this.graph.display();
    }

    public void addNode(final Node node) {
        this.graph.addNode(node.getIdAsString());
        this.setColor(node, Color.BLACK);
        this.setLabel(node);
        this.formatNode(node);
    }

    public void addEdge(final String id, final Node from, final Node to) {
        var displayEdge = this.graph.addEdge(String.valueOf(id), from.getIdAsString(), to.getIdAsString());
        this.formatEdge(displayEdge);
    }

    public void setColor(final Node node, final Color color) {
        this.graph.getNode(node.getIdAsString()).setAttribute("ui.style", "fill-color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "), rgb(255,255,255);");
    }

    public void setLabel(final Node node) {
        this.graph.getNode(node.getIdAsString()).setAttribute("ui.label", node.toString());
    }

    public void refreshNode(final Node node) {
        if (node.getSettledRobot() != null && node.getMobileRobot() == null) {
            this.setColor(node, Color.GREEN);
        }
        else  if (node.getSettledRobot() != null && node.getMobileRobot() != null) {
            this.setColor(node, Color.RED);
        }
        this.setLabel(node);
    }

    private void formatNode(final Node node) {
        var displayNode = this.graph.getNode(node.getIdAsString());
        displayNode.setAttribute("ui.style", "shape: box; size: 20px; stroke-mode: plain; text-alignment: under; text-size: 12px; fill-mode: gradient-diagonal1; stroke-mode: dashes;");
    }

    private void formatEdge(final Edge edge) {
        edge.setAttribute("ui.style", "shape: blob; fill-color: rgb(75,75,75);");
    }
}
