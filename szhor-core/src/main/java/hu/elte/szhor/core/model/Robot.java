package hu.elte.szhor.core.model;

import hu.elte.szhor.core.model.util.RobotState;
import hu.elte.szhor.core.util.Statistics;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Robot {

    private final int id;
    private final double chanceOfCrash;
    private final Graph graph;
    private final Statistics statistics;

    private RobotState state;
    private Node currentLocation;
    private Node markedLocation;

    public Robot(final int id, final Graph graph, final Node sourceNode, final double chanceOfCrash, final Statistics statistics) {
        this.id = id;
        this.graph = graph;
        this.state = RobotState.MOBILE;
        this.currentLocation = sourceNode;
        this.currentLocation.setMobileRobot(this);
        this.chanceOfCrash = chanceOfCrash;
        this.statistics = statistics;
    }

    public Robot(final int id, final Graph graph, final Node sourceNode, final double chanceOfCrash) {
        this(id, graph, sourceNode, chanceOfCrash, null);
    }

    public void action() {
        if (this.state != RobotState.MOBILE) {
            return;
        }

        final var neighbours = this.graph.getNeighbours(this.currentLocation);

        // Algorithm 1, Rule 1: if a neighbour u of v contains exactly one robot, and this robot marks v then Attempt to move to u
        final var markedNeighbours = new ArrayList<Node>(neighbours.size());
        for (var node : neighbours) {
            var settled = node.getSettledRobot();
            if (node.getMobileRobot() == null && settled != null && Objects.equals(settled.getMarkedLocation(), this.currentLocation)) {
                markedNeighbours.add(node);
            }
        }

        if (!markedNeighbours.isEmpty()) {
            var target = markedNeighbours.get(ThreadLocalRandom.current().nextInt(markedNeighbours.size()));
            if (this.hasCrashed()) {
                this.cleanUp();
            } else {
                this.moveTo(target);
            }
            return;
        }

        // Algorithm 1, Rule 2: else if a neighbour u of v contains no robots then Attempt to move to u and become settled if no crash. Mark v.
        final var emptyNeighbours = new ArrayList<Node>(neighbours.size());
        for (var node : neighbours) {
            if (!this.graph.isSourceNode(node) && node.getMobileRobot() == null && node.getSettledRobot() == null) {
                emptyNeighbours.add(node);
            }
        }

        if (!emptyNeighbours.isEmpty()) {
            var target = emptyNeighbours.get(ThreadLocalRandom.current().nextInt(emptyNeighbours.size()));
            if (this.hasCrashed()) {
                this.cleanUp();
            } else {
                this.moveToAndSettle(target);
            }
            return;
        }

        // Algorithm 1, Rule 3: else Stay put
    }

    private boolean hasCrashed() {
        return ThreadLocalRandom.current().nextDouble() < this.chanceOfCrash;
    }

    private void moveTo(final Node targetLocation) {
        this.currentLocation.setMobileRobot(null);
        this.currentLocation = targetLocation;
        this.currentLocation.setMobileRobot(this);
        if (this.statistics != null) {
            this.statistics.incrementMoves();
        }
    }

    private void moveToAndSettle(final Node targetLocation) {
        var previousLocation = this.currentLocation;
        this.markedLocation = previousLocation;
        this.currentLocation = targetLocation;
        this.currentLocation.setSettledRobot(this);
        previousLocation.setMobileRobot(null);
        this.state = RobotState.SETTLED;
        if (this.statistics != null) {
            this.statistics.incrementMoves();
        }
    }

    private void cleanUp() {
        this.currentLocation.setMobileRobot(null);
        this.state = RobotState.CRASHED;
        if (this.statistics != null) {
            this.statistics.incrementCrashes();
        }
    }

    public RobotState getState() {
        return this.state;
    }

    public Node getCurrentLocation() {
        return this.currentLocation;
    }

    public Node getMarkedLocation() {
        return this.markedLocation;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) return true;
        if (!(object instanceof Robot other)) return false;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    @Override
    public String toString() {
        return Integer.toString(this.id);
    }
}
