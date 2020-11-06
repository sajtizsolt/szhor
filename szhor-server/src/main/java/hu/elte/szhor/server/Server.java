package hu.elte.szhor.server;

import hu.elte.szhor.view.Window;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class Server {

    public static void main(String[] arguments) {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new SingleGraph("I can see dead pixels");
        graph.addNode("N1");
        graph.addNode("N2");
        graph.addEdge("E1", "N1", "N2");
        Viewer viewer = graph.display();
        graph.addNode("N3");
        graph.addNode("N4");
        graph.addEdge("E2", "N3", "N4");

        //var window = new Window(15);


    }
}
