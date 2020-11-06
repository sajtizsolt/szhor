package hu.elte.szhor.view;

import hu.elte.szhor.model.Node;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.awt.*;

public class MazeGraphDisplay {

    private Viewer viewer;
    private final Graph graph;

    public MazeGraphDisplay() {
        this.graph = new SingleGraph("Simulation of Fast Uniform Dispersion of a Crash-prone Swarm");
        System.setProperty("org.graphstream.ui", "swing");
    }

    public void display() {
        this.viewer = this.graph.display();
    }

    public void close() {
        this.viewer.close();
    }

    public void addNode(final Node node) {
        var displayedNode = this.graph.addNode(node.getIdAsString());
        displayedNode.setAttribute("ui.label", node.toString());
    }

    public void addEdge(final String id, final Node from, final Node to) {
        this.graph.addEdge(String.valueOf(id), from.getIdAsString(), to.getIdAsString());
    }

    public void setColor(final Node node, final Color color) {
        this.graph.getNode(node.getIdAsString()).setAttribute("ui.style", "fill-color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");");
    }
}
