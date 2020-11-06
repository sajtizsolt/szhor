package hu.elte.szhor.model;

import org.jgrapht.Graph;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Vertex {

    private final String label;
    private final Graph<Vertex, Edge> graph;
    private Robot mobileRobot;
    private Robot settledRobot;

    public Vertex(Graph<Vertex, Edge> graph, String label) {
        this.graph = graph;
        this.label = label;
    }

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

    public String getLabel() {
        return this.label;
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
