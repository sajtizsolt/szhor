package hu.elte.szhor.visualizer.controller;

import hu.elte.szhor.core.model.Graph;
import hu.elte.szhor.core.model.Robot;
import hu.elte.szhor.core.model.util.RobotState;
import hu.elte.szhor.core.util.ArgumentHandler;
import hu.elte.szhor.visualizer.view.MazeGraphDisplay;

import java.awt.*;
import java.util.LinkedList;

public class MazeGraphController {

    private final Graph model;
    private final MazeGraphDisplay display;

    public MazeGraphController(final Graph model, final MazeGraphDisplay display) {
        this.model = model;
        this.display = display;
        this.setUpDisplayedGraph();
        this.display.display();
    }

    public void startSimulation() throws InterruptedException {
        var activeRobots = new LinkedList<Robot>();
        while (!this.model.isEveryNodeOccupied()) {
            for (var robot : activeRobots) {
                Thread.sleep(ArgumentHandler.getThreadWait());
                robot.action();
                this.display.refreshNode(robot.getCurrentLocation());
            }

            activeRobots.removeIf(
                    robot -> robot.getState().equals(RobotState.SETTLED) || robot.getState().equals(RobotState.CRASHED)
            );

            for (var sourceNode : this.model.getSourceNodes()) {
                if (sourceNode.getMobileRobot() == null) {
                    var newRobot = new Robot(this.model, sourceNode, ArgumentHandler.getChanceOfCrash());
                    activeRobots.add(newRobot);
                }
            }
        }
    }

    private void setUpDisplayedGraph() {
        this.addNodes();
        this.addEdgesBetweenNodes();
        for (var node : this.model.getSourceNodes()) {
            this.display.setColor(node, Color.BLUE);
        }
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
