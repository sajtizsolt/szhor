package hu.elte.szhor.controller;

import hu.elte.szhor.model.MazeGraph;
import hu.elte.szhor.model.Node;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import java.awt.Color;

public class MazeGraphController {

    private final MazeGraph model;
    private final Graph display;

    public MazeGraphController(final MazeGraph model) {
        this.model = model;
        this.display = new SingleGraph("Simulation of Fast Uniform Dispersion of a Crash-prone Swarm");
        System.setProperty("org.graphstream.ui", "swing");
        this.setUpDisplayedGraph();
        this.display.display();
    }

    private void setUpDisplayedGraph() {
        this.addNodes();
        this.addEdgesBetweenNodes();
        this.setColor(this.model.getSourceNode(), Color.BLUE);
    }

    private void addNodes() {
        for (var node : this.model.getNodes()) {
            var displayedNode = this.display.addNode(node.getIdAsString());
            displayedNode.setAttribute("ui.label", node.toString());
        }
    }

    private void addEdgesBetweenNodes() {
        var edgeId = 0;
        for (var node : this.model.getNodes()) {
            for (var neighbour : this.model.getNeighbours(node)) {
                if (node.getId() < neighbour.getId()) {
                    this.display.addEdge(String.valueOf(edgeId++), node.getIdAsString(), neighbour.getIdAsString());
                }
            }
        }
    }

    private void setColor(final Node node, final Color color) {
        this.display.getNode(node.getIdAsString()).setAttribute("ui.style", "fill-color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");");
    }
}
