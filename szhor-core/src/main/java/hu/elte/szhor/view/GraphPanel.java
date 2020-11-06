package hu.elte.szhor.view;

import hu.elte.szhor.model.Edge;
import hu.elte.szhor.model.Vertex;
import hu.elte.szhor.util.GraphBuilderHelper;
import org.jgrapht.Graph;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    private final int size;
    private final int tileSize;
    private final Graph<Vertex, Edge> graph;
    private final Node[][] nodes;

    public GraphPanel(final int size, final int tileSize) {
        this.size = size;
        this.tileSize = tileSize;
        this.graph = GraphBuilderHelper.getGraph(this.size);
        this.nodes = new Node[this.size][this.size];
        this.configurePanel();
        this.addNodes();
    }

    private void configurePanel() {
        this.setLayout(new GridLayout(this.size, this.size));
        this.setSize(this.size * this.tileSize, this.size * this.tileSize);
    }

    private void addNodes() {
        for (var i = 0; i < this.size; ++i) {
            for (var j = 0; j < this.size; ++j) {
                //this.nodes[i][j] = new Node(tileSize, this.graph.vertexSet().);
                this.add(this.nodes[i][j]);
            }
        }
        this.nodes[this.size / 2][this.size / 2].setBackground(Color.BLACK);
    }
}
