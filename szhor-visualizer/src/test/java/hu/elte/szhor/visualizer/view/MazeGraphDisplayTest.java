package hu.elte.szhor.visualizer.view;

import hu.elte.szhor.core.model.Node;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.assertj.core.api.Assertions.assertThat;

class MazeGraphDisplayTest {

    @Test
    void shouldInitializeDisplayWithStylesheetAndAddNodesAndEdges() {
        var display = new MazeGraphDisplay();
        var graph = display.getGraph();

        assertThat(graph.getAttribute("ui.stylesheet")).isNotNull();

        var n0 = new Node(0, 0, 0);
        var n1 = new Node(1, 0, 1);

        display.addNode(n0);
        display.addNode(n1);
        display.addEdge("0-1", n0, n1);

        assertThat(graph.getNode("0")).isNotNull();
        assertThat(graph.getNode("1")).isNotNull();
        assertThat(graph.getEdge("0-1")).isNotNull();

        display.setColor(n0, Color.GREEN);
        var node0Style = graph.getNode("0").getAttribute("ui.style");
        assertThat(node0Style.toString()).contains("fill-color: rgb(0,255,0)");

        display.refreshNode(n0, true);
        var refreshedStyle = graph.getNode("0").getAttribute("ui.style");
        assertThat(refreshedStyle.toString()).contains("fill-color: rgb(0,0,255)");
    }
}
