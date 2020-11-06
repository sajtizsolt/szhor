package hu.elte.szhor.utils;

import java.util.Objects;

public class Node {

    private static int currentId = 0;

    private final int id;
    private final int x;
    private final int y;

    private Robot mobileRobot;
    private Robot settledRobot;

    private Node(final int id, final int x, final int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Node(final int x, final int y) {
        this.id = currentId++;
        this.x = x;
        this.y = y;
    }

    public static Node create(final int x, final int y) {
        return new Node(-1, x, y);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Node)) return false;

        var other = (Node) object;
        return Objects.equals(this.x, other.x)
                && Objects.equals(this.y, other.y);
    }

    public int getId() {
        return this.id;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Robot getMobileRobot() {
        return this.mobileRobot;
    }

    public void setMobileRobot(Robot mobileRobot) {
        this.mobileRobot = mobileRobot;
    }

    public Robot getSettledRobot() {
        return this.settledRobot;
    }

    public void setSettledRobot(Robot settledRobot) {
        this.settledRobot = settledRobot;
    }
}
