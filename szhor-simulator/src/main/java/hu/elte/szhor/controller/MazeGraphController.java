package hu.elte.szhor.controller;

import hu.elte.szhor.controller.util.RobotController;
import hu.elte.szhor.model.MazeGraph;
import hu.elte.szhor.utils.ArgumentHandler;
import hu.elte.szhor.view.MazeGraphDisplay;
import java.awt.Color;

public class MazeGraphController {

    private final MazeGraph model;
    private final MazeGraphDisplay display;

    public MazeGraphController(final MazeGraph model, final MazeGraphDisplay display) {
        this.model = model;
        this.display = display;
        this.setUpDisplayedGraph();
        this.display.display();
    }

    public void startSimulation() throws InterruptedException {
        while (!this.model.isEveryNodeOccupied()) {
            this.waitForOtherThreads();
            if (this.model.getSourceNode().getMobileRobot() != null) {
                continue;
            }
            RobotController.startNewRobot(this.model, this.display, ArgumentHandler.getChanceOfCrash());
        }
    }

    public void stopSimulation() {
        RobotController.interruptAllRobots();
    }

    private void waitForOtherThreads() throws InterruptedException {
        Thread.sleep(500);
    }

    private void setUpDisplayedGraph() {
        this.addNodes();
        this.addEdgesBetweenNodes();
        this.display.setColor(this.model.getSourceNode(), Color.BLUE);
    }

    private void addNodes() {
        for (var node : this.model.getNodes()) {
            this.display.addNode(node);
        }
    }

    private void addEdgesBetweenNodes() {
        var edgeId = 0;
        for (var node : this.model.getNodes()) {
            for (var neighbour : this.model.getNeighbours(node)) {
                if (node.getId() < neighbour.getId()) {
                    this.display.addEdge(String.valueOf(edgeId++), node, neighbour);
                }
            }
        }
    }
}
