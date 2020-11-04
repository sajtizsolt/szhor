package hu.elte.szhor.model;

import hu.elte.szhor.util.RobotState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Robot implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Robot.class);

    private static int currentId = 0;

    private final int id;
    private RobotState state;
    private final int chanceOfCrash;
    private Vertex currentLocation;
    private Vertex markedLocation;

    public Robot(Vertex startingLocation, final float chanceOfCrash) {
        this.id = currentId++;
        this.state = RobotState.MOBILE;
        this.currentLocation = startingLocation;
        this.currentLocation.setMobileRobot(this);
        this.chanceOfCrash = (int) (100 * chanceOfCrash);
    }

    public boolean action() {
        for (var vertex : this.currentLocation.getNeighbours()) {
            // If a neighbour of current location contains exactly ONE robot, and this robot marks the current
            // location, then attempt to move to that vertex
            if (vertex.getMobileRobot() == null && vertex.getSettledRobot() != null && vertex.getSettledRobot().markedLocation.equals(this.currentLocation)) {
                if (hasCrashed()) {
                    LOGGER.info("Robot with id {} crashed.", this.id);
                    return false;
                }
                this.moveTo(vertex);
                return true;
            }
            // If a neighbour of current location contains NO robot, then attempt to move to that vertex and settle
            // down while marking the previous location
            else if (vertex.getMobileRobot() == null && vertex.getSettledRobot() == null) {
                if (hasCrashed()) {
                    LOGGER.info("Robot with id {} crashed.", this.id);
                    return false;
                }
                this.moveToAndSettle(vertex);
                return true;
            }
        }
        // Stay put
        return true;
    }

    private boolean hasCrashed() {
        return ThreadLocalRandom.current().nextInt(0, 100) < this.chanceOfCrash;
    }

    private void moveTo(Vertex targetLocation) {
        this.currentLocation.setMobileRobot(null);
        this.currentLocation = targetLocation;
        this.currentLocation.setMobileRobot(this);
        LOGGER.info("Robot with id {} moved to new location: {}.", this.id, targetLocation.getLabel());
    }

    private void moveToAndSettle(Vertex targetLocation) {
        var previousLocation = this.currentLocation;
        this.markedLocation = previousLocation;
        this.currentLocation = targetLocation;
        this.currentLocation.setSettledRobot(this);
        previousLocation.setMobileRobot(null);
        this.state = RobotState.SETTLED;
        LOGGER.info("Robot with id {} moved and settled at new location: {}.", this.id, targetLocation.getLabel());
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

    @Override
    public void run() {
        while (this.state == RobotState.MOBILE) {
            try {
                var time = (long) (ThreadLocalRandom.current().nextInt(0, 2) * 1000);
                Thread.sleep(time);
            } catch (InterruptedException e) {
                LOGGER.info("Robot with id {} is interrupted!", this.id);
                return;
            }
            this.action();
        }
        LOGGER.info("Robot with id {} finished!", this.id);
    }

    public int getId() {
        return this.id;
    }

    public RobotState getState() {
        return this.state;
    }
}
