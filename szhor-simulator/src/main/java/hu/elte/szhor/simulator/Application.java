package hu.elte.szhor.simulator;

import hu.elte.szhor.core.model.Graph;
import hu.elte.szhor.core.model.Robot;
import hu.elte.szhor.core.model.util.GraphBuilder;
import hu.elte.szhor.core.model.util.RobotState;
import hu.elte.szhor.core.util.ArgumentHandler;
import hu.elte.szhor.core.util.Statistics;

import java.io.IOException;
import java.util.LinkedList;

public class Application {

    public static void main(final String[] arguments) throws IOException {
        ArgumentHandler.validate(arguments);

        final var graph = GraphBuilder.fromFile(ArgumentHandler.getInputFile(), ArgumentHandler.getNumberOfSources());

        startSimulation(graph);

        Statistics.saveToFile(ArgumentHandler.getOutputFile());
    }

    private static void startSimulation(final Graph graph) {
        final var activeRobots = new LinkedList<Robot>();
        Statistics.executionTime = 0;
        while (!graph.isEveryNodeOccupied()) {
            for (var robot : activeRobots) {
                robot.action();
            }

            activeRobots.removeIf(
                robot -> robot.getState().equals(RobotState.SETTLED) || robot.getState().equals(RobotState.CRASHED)
            );

            for (var sourceNode : graph.getSourceNodes()) {
                if (sourceNode.getMobileRobot() == null) {
                    var newRobot = new Robot(graph, sourceNode, ArgumentHandler.getChanceOfCrash());
                    activeRobots.add(newRobot);
                    Statistics.numberOfRobots++;
                }
            }
            ++Statistics.executionTime;
        }
    }
}
