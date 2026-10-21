package hu.elte.szhor.visualizer.controller;

import hu.elte.szhor.core.model.Graph;
import hu.elte.szhor.core.model.Robot;
import hu.elte.szhor.core.model.util.RobotState;
import hu.elte.szhor.visualizer.view.MazeGraphDisplay;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class MazeGraphController {

    private final Graph model;
    private final MazeGraphDisplay display;
    private final int threadWait;
    private final double chanceOfCrash;

    public MazeGraphController(final Graph model, final MazeGraphDisplay display, final int threadWait, final double chanceOfCrash) {
        this.model = model;
        this.display = display;
        this.threadWait = threadWait;
        this.chanceOfCrash = chanceOfCrash;
        this.setUpDisplayedGraph();
        this.display.display();
    }

    public MazeGraphController(final Graph model, final MazeGraphDisplay display) {
        this(model, display, 100, 0.0);
    }

    public void startSimulation() throws InterruptedException {
        var activeRobots = new ArrayList<Robot>();
        var robotId = 0;
        while (!this.model.isEveryNodeOccupied()) {
            Collections.shuffle(activeRobots);
            for (var robot : activeRobots) {
                Thread.sleep(this.threadWait);
                var prevLocation = robot.getCurrentLocation();
                robot.action();
                var currLocation = robot.getCurrentLocation();

                this.display.refreshNode(prevLocation, this.model.isSourceNode(prevLocation));
                this.display.refreshNode(currLocation, this.model.isSourceNode(currLocation));
            }

            activeRobots.removeIf(
                robot -> robot.getState() == RobotState.SETTLED || robot.getState() == RobotState.CRASHED
            );

            for (var sourceNode : this.model.getSourceNodes()) {
                if (sourceNode.getMobileRobot() == null) {
                    var newRobot = new Robot(robotId++, this.model, sourceNode, this.chanceOfCrash);
                    activeRobots.add(newRobot);
                    this.display.refreshNode(sourceNode, true);
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
