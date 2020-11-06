package hu.elte.szhor.view;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private static final int TILE_SIZE = 50;

    private final int size;
    private final JPanel statusPanel;
    private final JPanel graphPanel;

    public Window(final int size) {
        this.size = size;
        this.statusPanel = new StatusPanel();
        this.graphPanel = new GraphPanel(this.size, TILE_SIZE);
        this.configureWindow();
    }

    public void refresh() {

    }

    private void configureWindow() {
        this.setTitle("Simulation of Fast Uniform Dispersion of a Crash-prone Swarm");
        this.setSize(this.calculateWidth(), this.calculateHeight());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout();
        this.setResizable(false);
        this.setVisible(true);
    }

    private void setLayout() {
        var contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(statusPanel, BorderLayout.NORTH);
        contentPane.add(graphPanel, BorderLayout.CENTER);
    }

    private int calculateWidth() {
        return this.statusPanel.getWidth() + this.graphPanel.getWidth();
    }

    private int calculateHeight() {
        return this.statusPanel.getHeight() + this.graphPanel.getHeight();
    }
}
