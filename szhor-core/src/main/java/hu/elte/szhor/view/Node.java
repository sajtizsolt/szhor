package hu.elte.szhor.view;

import hu.elte.szhor.model.Vertex;

import javax.swing.*;
import java.awt.*;

public class Node extends JPanel {

    private final int size;
    private final Vertex vertex;

    public Node(final int size, final Vertex vertex) {
        this.size = size;
        this.vertex = vertex;
        this.configurePanel();
    }

    private void configurePanel() {
        this.setSize(this.size, this.size);
        this.setBackground(Color.WHITE);
        //this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
