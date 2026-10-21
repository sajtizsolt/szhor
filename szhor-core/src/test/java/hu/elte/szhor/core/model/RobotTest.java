package hu.elte.szhor.core.model;

import hu.elte.szhor.core.model.util.RobotState;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RobotTest {

    @Test
    void shouldMoveAndSettleOnEmptyNeighbour() {
        var source = new Node(0, 0, 0);
        var target = new Node(1, 0, 1);
        var nodes = List.of(source, target);

        var matrix = new int[2][2];
        matrix[0][1] = 1;
        matrix[1][0] = 1;

        var sources = List.of(source);
        var graph = new Graph(nodes, matrix, sources);

        var robot = new Robot(1, graph, source, 0);
        assertThat(robot.getState()).isEqualTo(RobotState.MOBILE);
        assertThat(source.getMobileRobot()).isEqualTo(robot);

        robot.action();

        assertThat(robot.getState()).isEqualTo(RobotState.SETTLED);
        assertThat(source.getMobileRobot()).isNull();
        assertThat(target.getSettledRobot()).isEqualTo(robot);
    }

    @Test
    void shouldMoveThroughAlreadySettledRobotToUnsettledNode() {
        var n0 = new Node(0, 0, 0); // Source
        var n1 = new Node(1, 0, 1); // Intermediate
        var n2 = new Node(2, 0, 2); // Far target

        var adjacencyList = Map.of(
            n0, List.of(n1),
            n1, List.of(n0, n2),
            n2, List.of(n1)
        );

        var graph = new Graph(List.of(n0, n1, n2), adjacencyList, List.of(n0));

        // 1. First robot enters and settles on n1
        var robot1 = new Robot(1, graph, n0, 0.0);
        robot1.action();

        assertThat(robot1.getState()).isEqualTo(RobotState.SETTLED);
        assertThat(n1.getSettledRobot()).isEqualTo(robot1);
        assertThat(robot1.getMarkedLocation()).isEqualTo(n0);
        assertThat(n0.getMobileRobot()).isNull();

        // 2. Second robot spawns on n0
        var robot2 = new Robot(2, graph, n0, 0.0);
        assertThat(n0.getMobileRobot()).isEqualTo(robot2);

        // First action: robot2 sees robot1 settled with marked location = n0, so it hops to n1
        robot2.action();

        assertThat(robot2.getState()).isEqualTo(RobotState.MOBILE);
        assertThat(robot2.getCurrentLocation()).isEqualTo(n1);
        assertThat(n1.getMobileRobot()).isEqualTo(robot2);
        assertThat(n1.getSettledRobot()).isEqualTo(robot1);
        assertThat(n0.getMobileRobot()).isNull();

        // Second action: robot2 moves from n1 to empty target n2 and settles
        robot2.action();

        assertThat(robot2.getState()).isEqualTo(RobotState.SETTLED);
        assertThat(robot2.getCurrentLocation()).isEqualTo(n2);
        assertThat(n2.getSettledRobot()).isEqualTo(robot2);
        assertThat(n1.getMobileRobot()).isNull();
        assertThat(graph.isEveryNodeOccupied()).isTrue();
    }

    @Test
    void shouldPrioritizeMarkedNeighborOverEmptyNeighborAccordingToAlgorithm1() {
        var v = new Node(0, 0, 0); // Source / current position
        var uMarked = new Node(1, 0, 1);
        var uEmpty = new Node(2, 1, 0);

        var adjacencyList = Map.of(
            v, List.of(uMarked, uEmpty),
            uMarked, List.of(v),
            uEmpty, List.of(v)
        );
        var graph = new Graph(List.of(v, uMarked, uEmpty), adjacencyList, List.of(v));

        // Settle a robot at uMarked that marks v
        var settledRobot = new Robot(1, graph, v, 0.0);
        settledRobot.action(); // settles at uMarked (or uEmpty)
        // Ensure uMarked specifically has the settled robot marking v
        v.setMobileRobot(null);
        uMarked.setSettledRobot(settledRobot);
        uEmpty.setSettledRobot(null);
        uEmpty.setMobileRobot(null);

        // A new mobile robot enters at v
        var mobileRobot = new Robot(2, graph, v, 0.0);
        assertThat(v.getMobileRobot()).isEqualTo(mobileRobot);

        // Algorithm 1 strictly prioritizes uMarked over uEmpty
        mobileRobot.action();

        assertThat(mobileRobot.getState()).isEqualTo(RobotState.MOBILE);
        assertThat(mobileRobot.getCurrentLocation()).isEqualTo(uMarked);
        assertThat(uMarked.getMobileRobot()).isEqualTo(mobileRobot);
        assertThat(uEmpty.getSettledRobot()).isNull();
        assertThat(uEmpty.getMobileRobot()).isNull();
    }

    @Test
    void shouldCleanUpWhenRobotCrashes() {
        var source = new Node(0, 0, 0);
        var target = new Node(1, 0, 1);
        var nodes = List.of(source, target);

        var matrix = new int[2][2];
        matrix[0][1] = 1;
        matrix[1][0] = 1;

        var sources = List.of(source);
        var graph = new Graph(nodes, matrix, sources);

        // 100% chance of crash
        var robot = new Robot(1, graph, source, 1.0);
        robot.action();

        assertThat(robot.getState()).isEqualTo(RobotState.CRASHED);
        assertThat(source.getMobileRobot()).isNull();
        assertThat(target.getSettledRobot()).isNull();
    }

    @Test
    void shouldMaintainStableIdentityAcrossStateTransitions() {
        var node = new Node(0, 0, 0);
        var graph = new Graph(List.of(node), new int[1][1], List.of(node));
        var robot = new Robot(1, graph, node, 0.0);

        var set = new HashSet<Robot>();
        set.add(robot);

        assertThat(set).contains(robot);
        assertThat(robot).hasSameHashCodeAs(robot);
    }
}
