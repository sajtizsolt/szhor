package hu.elte.szhor.model;

import org.jgrapht.Graph;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class represent a vertex of the graph which represents the environment.
 */
public class Vertex {

    /**
     * The label of the vertex. Doesn't have to be unique.
     */
    private final String label;

    /**
     * The graph which contains the vertex.
     */
    private final Graph<Vertex, Edge> graph;

    /**
     * The robot which is currently staying at the vertex in MOBILE state.
     */
    private Robot mobileRobot;

    /**
     * The robot which SETTLED down at the vertex.
     */
    private Robot settledRobot;

    /**
     * Constructs a new Vertex instance.
     * @param graph The graph which will contain the vertex.
     * @param label The label of the vertex.
     */
    public Vertex(Graph<Vertex, Edge> graph, String label) {
        this.graph = graph;
        this.label = label;
    }

    /**
     * Returns the neighbours of the vertex.
     * @return The neighbouring vertices.
     */
    public Set<Vertex> getNeighbours() {
        var resultSet = new HashSet<Vertex>();
        for (var edge : graph.outgoingEdgesOf(this)) {
            resultSet.add(edge.getTargetVertex());
        }
        for (var edge : graph.incomingEdgesOf(this)) {
            resultSet.add(edge.getTargetVertex());
        }
        return resultSet;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Vertex)) return false;

        var other = (Vertex) object;
        return Objects.equals(this.label, other.label)
            && Objects.equals(this.graph, other.graph)
            && this.mobileRobot.equals(other.mobileRobot)
            && this.settledRobot.equals(other.settledRobot);
    }

    @Override
    public String toString() {
        return this.label + " M=" + (this.mobileRobot != null ? this.mobileRobot.getId() : "#") + " S=" + (this.settledRobot != null ? this.settledRobot.getId() : "#");
    }

    /**
     * Returns the label of the vertex.
     * @return The label of the vertex.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Returns the mobile robot of the vertex.
     * @return The mobile robot of the vertex.
     */
    public Robot getMobileRobot() {
        return this.mobileRobot;
    }

    /**
     * Sets the mobile robot of the vertex.
     * @param mobileRobot The new mobile robot.
     */
    public void setMobileRobot(Robot mobileRobot) {
        this.mobileRobot = mobileRobot;
    }

    /**
     * Returns the settled robot of the vertex.
     * @return The settled robot of the vertex.
     */
    public Robot getSettledRobot() {
        return this.settledRobot;
    }

    /**
     * Sets the settled robot of the vertex.
     * @param settledRobot The bew settled robot.
     */
    public void setSettledRobot(Robot settledRobot) {
        this.settledRobot = settledRobot;
    }
}
