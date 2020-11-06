package hu.elte.szhor.model;

import org.jgrapht.graph.DefaultEdge;
import java.util.Objects;

public class Edge extends DefaultEdge {

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Edge)) return false;

        var other = (Edge) object;
        return Objects.equals(this.getSourceVertex(), other.getSourceVertex())
            && Objects.equals(this.getTargetVertex(), other.getTargetVertex());
    }

    @Override
    public String toString() {
        return this.getSourceVertex().toString() + "   " + this.getTargetVertex().toString();
    }

    public Vertex getSourceVertex() {
        return ((Vertex) super.getSource());
    }

    public Vertex getTargetVertex() {
        return ((Vertex) super.getTarget());
    }
}
