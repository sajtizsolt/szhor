package hu.elte.szhor.core.model;

public class Node {

    private final int id;
    private final int x;
    private final int y;

    private Robot mobileRobot;
    private Robot settledRobot;

    public Node(final int id, final int x, final int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Node(final int x, final int y) {
        this(0, x, y);
    }

    public static Node create(final int x, final int y) {
        return new Node(x, y);
    }

    public String getIdAsString() {
        return Integer.toString(this.id);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) return true;
        if (!(object instanceof Node other)) return false;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * this.x + this.y;
    }

    @Override
    public String toString() {
        return this.id + "[" + (this.settledRobot == null ? "-" : this.settledRobot) + ";" + (this.mobileRobot == null ? "-" : this.mobileRobot) + "]";
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

    public void setMobileRobot(final Robot mobileRobot) {
        this.mobileRobot = mobileRobot;
    }

    public Robot getSettledRobot() {
        return this.settledRobot;
    }

    public void setSettledRobot(final Robot settledRobot) {
        this.settledRobot = settledRobot;
    }
}
