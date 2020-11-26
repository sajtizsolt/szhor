package hu.elte.szhor.core.model;

import hu.elte.szhor.core.model.util.RobotState;
import hu.elte.szhor.core.util.Statistics;

import java.util.Objects;
import java.util.Random;

public class Robot {

    private static final Random RANDOM_GENERATOR = new Random();

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

    public void action() {
        if (!this.state.equals(RobotState.MOBILE)) {
            return;
        }
        var neighbours = this.graph.getNeighbours(this.currentLocation);
        while (true) {
            if (neighbours.isEmpty()) {
                return;
            }
            var node = neighbours.remove(new Random().nextInt(neighbours.size()));
            if (node.getMobileRobot() == null && node.getSettledRobot() != null && node.getSettledRobot().markedLocation.equals(this.currentLocation)) {
                if (this.hasCrashed()) {
                    this.cleanUp();
                }
                else {
                    this.moveTo(node);
                }
                return;
            }
            else if (!this.graph.getSourceNodes().contains(node) && node.getMobileRobot() == null && node.getSettledRobot() == null) {
                if (this.hasCrashed()) {
                    this.cleanUp();
                }
                else {
                    this.moveToAndSettle(node);
                }
                return;
            }
        }
    }

    private boolean hasCrashed() {
        return RANDOM_GENERATOR.nextInt(100) < this.chanceOfCrash;
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
