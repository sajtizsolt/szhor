package hu.elte.szhor.simulator;

import hu.elte.szhor.core.model.Graph;
import hu.elte.szhor.core.model.Robot;
import hu.elte.szhor.core.model.util.RobotState;
import hu.elte.szhor.core.util.Statistics;

import java.util.ArrayList;
import java.util.Collections;

public class Simulation {

    private final Graph graph;
    private final double chanceOfCrash;
    private final Statistics statistics;

    public Simulation(final Graph graph, final double chanceOfCrash) {
        this.graph = graph;
        this.chanceOfCrash = chanceOfCrash;
        this.statistics = new Statistics();
        this.statistics.setNumberOfNodes(graph.getNodes().size());
        this.statistics.setNumberOfSources(graph.getSourceNodes().size());
        this.statistics.setChanceOfCrash(chanceOfCrash);
        this.statistics.setNumberOfEdges(graph.getNumberOfEdges());
    }

    public Statistics run() {
        final var activeRobots = new ArrayList<Robot>();
        var robotId = 0;
        this.statistics.setExecutionTime(0);

        while (!this.graph.isEveryNodeOccupied()) {
            if (Thread.currentThread().isInterrupted()) {
                throw new IllegalStateException("Simulation interrupted");
            }
            Collections.shuffle(activeRobots);
            for (var robot : activeRobots) {
                robot.action();
            }

            activeRobots.removeIf(
                robot -> robot.getState() == RobotState.SETTLED || robot.getState() == RobotState.CRASHED
            );

            for (var sourceNode : this.graph.getSourceNodes()) {
                if (sourceNode.getMobileRobot() == null) {
                    var newRobot = new Robot(robotId++, this.graph, sourceNode, this.chanceOfCrash, this.statistics);
                    activeRobots.add(newRobot);
                    this.statistics.incrementRobots();
                }
            }
            this.statistics.incrementExecutionTime();
        }
        return this.statistics;
    }

    public Statistics getStatistics() {
        return this.statistics;
    }

    public Graph getGraph() {
        return this.graph;
    }
}
