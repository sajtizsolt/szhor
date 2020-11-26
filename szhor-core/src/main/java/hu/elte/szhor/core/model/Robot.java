package hu.elte.szhor.core.model;

import hu.elte.szhor.core.model.util.RobotState;
import hu.elte.szhor.core.util.Statistics;

import java.util.Objects;
import java.util.Random;

public class Robot {

    private static int currentId = 0;

    private final int id;
    private final int chanceOfCrash;

    private final Graph graph;

    private RobotState state;
    private Node currentLocation;
    private Node markedLocation;

    public Robot(final Graph graph, final Node sourceNode, final int chanceOfCrash) {
        this.id = currentId++;
        this.graph = graph;
        this.state = RobotState.MOBILE;
        this.currentLocation = sourceNode;
        this.currentLocation.setMobileRobot(this);
        this.chanceOfCrash = chanceOfCrash;
    }

    public void tryToMove() {
        if (!this.state.equals(RobotState.MOBILE)) {
            return;
        }
        if (this.graph.getSourceNodes().contains(this.currentLocation)) {
            this.takeFirstStep();
        }
        else {
            this.action();
        }
    }

    public void action() {
        for (var node : this.graph.getNeighbours(this.currentLocation)) {
            // If a neighbour of current location contains exactly ONE robot, and this robot marks the current
            // location, then attempt to move to that node
            if (node.getMobileRobot() == null && node.getSettledRobot() != null && node.getSettledRobot().markedLocation.equals(this.currentLocation)) {
                if (this.hasCrashed()) {
                    this.cleanUp();
                }
                else {
                    this.moveTo(node);
                }
                break;
            }
            // If a neighbour of current location contains NO robot, then attempt to move to that node and settle
            // down while marking the previous location
            else if (!this.graph.getSourceNodes().contains(node) && node.getMobileRobot() == null && node.getSettledRobot() == null) {
                if (this.hasCrashed()) {
                    this.cleanUp();
                }
                else {
                    this.moveToAndSettle(node);
                }
                break;
            }
        }
    }

    public void takeFirstStep() {
        var success = false;
        var neighbours = this.graph.getNeighbours(this.currentLocation);
        while (!success) {
            var node = neighbours.get(new Random().nextInt(neighbours.size()));
            // If a neighbour of current location contains exactly ONE robot, and this robot marks the current
            // location, then attempt to move to that node
            if (node.getMobileRobot() == null && node.getSettledRobot() != null && node.getSettledRobot().markedLocation.equals(this.currentLocation)) {
                if (this.hasCrashed()) {
                    this.cleanUp();
                }
                else {
                    this.moveTo(node);
                }
                success = true;
            }
            // If a neighbour of current location contains NO robot, then attempt to move to that node and settle
            // down while marking the previous location
            else if (!this.graph.getSourceNodes().contains(node) && node.getMobileRobot() == null && node.getSettledRobot() == null) {
                if (this.hasCrashed()) {
                    this.cleanUp();
                }
                else {
                    this.moveToAndSettle(node);
                }
                success = true;
            }
        }
    }

    private boolean hasCrashed() {
        return new Random().nextInt(100) < this.chanceOfCrash;
    }

    private void moveTo(final Node targetLocation) {
        this.currentLocation.setMobileRobot(null);
        this.currentLocation = targetLocation;
        this.currentLocation.setMobileRobot(this);
        Statistics.numberOfMoves++;
    }

    private void moveToAndSettle(final Node targetLocation) {
        var previousLocation = this.currentLocation;
        this.markedLocation = previousLocation;
        this.currentLocation = targetLocation;
        this.currentLocation.setSettledRobot(this);
        previousLocation.setMobileRobot(null);
        this.state = RobotState.SETTLED;
        Statistics.numberOfMoves++;
    }

    private void cleanUp() {
        this.currentLocation.setMobileRobot(null);
        this.state = RobotState.CRASHED;
        Statistics.numberOfCrashes++;
    }

    public RobotState getState() {
        return this.state;
    }

    public Node getCurrentLocation() {
        return this.currentLocation;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Robot)) return false;

        var other = (Robot) object;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.state, other.state);
    }

    @Override
    public String toString() {
        return "" + this.id;
    }
}
