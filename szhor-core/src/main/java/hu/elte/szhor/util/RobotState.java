package hu.elte.szhor.util;

/**
 * This enum represents the state of a robot in the swarm.
 */
public enum RobotState {
    /**
     * The robot is currently in mobile state, so it can move.
     */
    MOBILE,

    /**
     * The robot is settled down, so it can't move.
     */
    SETTLED
}
