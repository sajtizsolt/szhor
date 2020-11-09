package hu.elte.szhor.model;

import hu.elte.szhor.util.RobotState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents an asynchronous mobile robot.
 */
public class Robot implements Runnable {

        private static final Logger LOGGER = LoggerFactory.getLogger(Robot.class);

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
         * The chance with which the robot will crash when it moves.
         */
        private final int chanceOfCrash;

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
         * @param startingLocation The starting location of the robot.
         * @param chanceOfCrash The chance with which
         */
    public Robot(Vertex startingLocation, final float chanceOfCrash){
                this.id = currentId++;
                this.state = RobotState.MOBILE;
                this.currentLocation = startingLocation;
                this.currentLocation.setMobileRobot(this);
                this.chanceOfCrash = (int) (100 * chanceOfCrash);
            }

            /**
             * Called when the robot should activate.
             * Makes action based on the local rule of behaviour.
             * @return True if the robot didn't crash while taking action.
             */
            public boolean action () {
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

            /**
             * Returns true if the robot crashes.
             * @return True if the robot crashes.
             */
            private boolean hasCrashed () {
                return ThreadLocalRandom.current().nextInt(0, 100) < this.chanceOfCrash;
            }

            /**
             * The robot leaves the current location and moves to the target location.
             * @param targetLocation The new location of the robot.
             */
            private void moveTo (Vertex targetLocation){
                this.currentLocation.setMobileRobot(null);
                this.currentLocation = targetLocation;
                this.currentLocation.setMobileRobot(this);
                LOGGER.info("Robot with id {} moved to new location: {}.", this.id, targetLocation.getLabel());
            }

            /**
             * The robot leaves the current location and moves to the target location.
             * After moving, it settles down.
             * @param targetLocation The new location of the robot.
             */
            private void moveToAndSettle (Vertex targetLocation){
                var previousLocation = this.currentLocation;
                this.markedLocation = previousLocation;
                this.currentLocation = targetLocation;
                this.currentLocation.setSettledRobot(this);
                previousLocation.setMobileRobot(null);
                this.state = RobotState.SETTLED;
                LOGGER.info("Robot with id {} moved and settled at new location: {}.", this.id, targetLocation.getLabel());
            }

            @Override
            public boolean equals (Object object){
                if (object == null) return false;
                if (object == this) return true;
                if (!(object instanceof Robot)) return false;
                var other = (Robot) object;
                return Objects.equals(this.id, other.id)
                        && Objects.equals(this.state, other.state);
            }

            @Override
            public void run () {
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

            /**
             * Returns the id of the robot.
             * @return The id of the robot.
             */
            public int getId () {
                return this.id;
            }

            /**
             * Returns the state of the robot.
             * @return The state of the robot.
             */
            public RobotState getState () {
                return this.state;
            }
        }