package hu.elte.szhor.model;

import hu.elte.szhor.util.RobotState;
import java.util.Objects;

/**
 * This class represents an asynchronous mobile robot.
 */
public class Robot {

    /**
     * The unique identifier of the instance.
     */
    private final int id;

    /**
     * A static field which helps giving identifiers.
     */
    private static int currentId = 0;

    /**
     * The current state of the robot.
     */
    private RobotState state;

    /**
     * The current location of the robot.
     */
    private Vertex currentLocation;

    /**
     * The location marked by the settled robot.
     */
    private Vertex markedLocation;

    /**
     * The constructor of the class.
     */
    public Robot(Vertex startingLocation) {
        this.id = currentId++;
        this.state = RobotState.MOBILE;
        this.currentLocation = startingLocation;
    }

    /**
     * Called when the robot should activate.
     * Makes action based on the local rule of behaviour.
     * @return True if the robot didn't crash while taking action.
     */
    public boolean action() {
        for (var vertex : this.currentLocation.getNeighbours()) {
            // If a neighbour of current location contains exactly ONE robot, and this robot marks the current
            // location, then attempt to move to that vertex
            if (vertex.getMobileRobot() == null && vertex.getSettledRobot() != null && vertex.getSettledRobot().markedLocation.equals(this.currentLocation)) {
                if (hasCrashed()) {
                    return false;
                }
                this.moveTo(vertex);
                System.out.println("(" + this.id + ") Moved to " + this.currentLocation.getLabel());
                return true;
            }
            // If a neighbour of current location contains NO robot, then attempt to move to that vertex and settle
            // down while marking the previous location
            else if (vertex.getMobileRobot() == null && vertex.getSettledRobot() == null) {
                if (hasCrashed()) {
                    return false;
                }
                this.moveToAndSettle(vertex);
                System.out.println("(" + this.id + ") Moved and settled at " + this.currentLocation.getLabel());
                return true;
            }
        }
        // Stay put
        System.out.println("(" + this.id + ") Stayed at " + this.currentLocation.getLabel());
        return true;
    }

    private boolean hasCrashed() {
        return false;
    }

    private void moveTo(Vertex targetLocation) {
        this.currentLocation.setMobileRobot(null);
        this.currentLocation = targetLocation;
        this.currentLocation.setMobileRobot(this);
    }

    private void moveToAndSettle(Vertex targetLocation) {
        this.currentLocation.setMobileRobot(null);
        this.markedLocation = this.currentLocation;
        this.currentLocation = targetLocation;
        this.currentLocation.setSettledRobot(this);
        this.state = RobotState.SETTLED;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Robot)) return false;

        var other = (Robot) object;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.state, other.state);
    }

    public RobotState getState() {
        return this.state;
    }
}
