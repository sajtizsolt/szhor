package hu.elte.szhor.model;

import hu.elte.szhor.model.util.RobotState;
import hu.elte.szhor.utils.Statistics;
import hu.elte.szhor.view.MazeGraphDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Robot implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Robot.class);

    private static int currentId = 0;

    private final int id;
    private final int chanceOfCrash;
    private final MazeGraph graph;
    private final MazeGraphDisplay display;

    private RobotState state;
    private Node currentLocation;
    private Node markedLocation;

    public Robot(final MazeGraph graph, final MazeGraphDisplay display, final int chanceOfCrash) {
        this.id = currentId++;
        this.graph = graph;
        this.display = display;
        this.state = RobotState.MOBILE;
        this.currentLocation = this.graph.getSourceNode();
        this.currentLocation.setMobileRobot(this);
        this.chanceOfCrash = chanceOfCrash;
    }

    public void action() {
        for (var node : this.graph.getNeighbours(this.currentLocation)) {
            // If a neighbour of current location contains exactly ONE robot, and this robot marks the current
            // location, then attempt to move to that node
            if (node.getMobileRobot() == null && node.getSettledRobot() != null && node.getSettledRobot().markedLocation.equals(this.currentLocation)) {
                if (this.hasCrashed()) {
                    LOGGER.info("Robot with id {} crashed.", this.id);
                    Statistics.numberOfCrashes++;
                    this.cleanUp();
                }
                this.moveTo(node);
                Statistics.numberOfMoves++;
                return;
            }
            // If a neighbour of current location contains NO robot, then attempt to move to that node and settle
            // down while marking the previous location
            else if (!node.equals(this.graph.getSourceNode()) && node.getMobileRobot() == null && node.getSettledRobot() == null) {
                if (this.hasCrashed()) {
                    LOGGER.info("Robot with id {} crashed.", this.id);
                    Statistics.numberOfCrashes++;
                    this.cleanUp();
                }
                this.moveToAndSettle(node);
                Statistics.numberOfMoves++;
                return;
            }
        }
    }

    private boolean hasCrashed() {
        return ThreadLocalRandom.current().nextInt(0, 100) < this.chanceOfCrash;
    }

    private void moveTo(Node targetLocation) {
        this.currentLocation.setMobileRobot(null);
        this.setColor(this.currentLocation);
        this.currentLocation = targetLocation;
        this.currentLocation.setMobileRobot(this);
        LOGGER.info("Robot with id {} moved to new location: {}.", this.id, targetLocation.getId());
        this.setColor(this.currentLocation);
    }

    private void moveToAndSettle(Node targetLocation) {
        var previousLocation = this.currentLocation;
        this.markedLocation = previousLocation;
        this.setColor(previousLocation);
        this.currentLocation = targetLocation;
        this.currentLocation.setSettledRobot(this);
        previousLocation.setMobileRobot(null);
        this.state = RobotState.SETTLED;
        LOGGER.info("Robot with id {} moved and settled at new location: {}.", this.id, targetLocation.getId());
        this.setColor(this.currentLocation);
    }

    private void cleanUp() {
        this.currentLocation.setMobileRobot(null);
        this.setColor(this.currentLocation);
        this.state = RobotState.CRASHED;
        Thread.currentThread().interrupt();
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
    public String toString() {
        return "" + this.getId();
    }

    @Override
    public void run() {
        while (this.state == RobotState.MOBILE) {
            try {
                //var time = (long) (ThreadLocalRandom.current().nextInt(0, 2) * 1000);
                //Thread.sleep(time);
                synchronized (this.graph) {
                    this.action();
                }
            }
            catch (Exception e) {
                LOGGER.info("Robot with id {} is interrupted!", this.id);
                this.cleanUp();
                return;
            }
        }
        LOGGER.info("Robot with id {} finished!", this.id);
        this.cleanUp();
    }

    public int getId() {
        return this.id;
    }

    private void setColor(final Node node) {
        if (node.equals(this.graph.getSourceNode())) {
            this.display.setColor(node, Color.BLUE);
        }
        else if (node.getMobileRobot() == null && node.getSettledRobot() == null) {
            this.display.setColor(node, Color.BLACK);
        }
        else if (node.getMobileRobot() == null && node.getSettledRobot() != null) {
            this.display.setColor(node, Color.GREEN);
        }
        else if (node.getMobileRobot() != null && node.getSettledRobot() != null) {
            this.display.setColor(node, Color.RED);
        }
    }
}
