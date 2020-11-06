package hu.elte.szhor;

import hu.elte.szhor.utils.GraphModel;
import hu.elte.szhor.utils.Robot;
import org.graphstream.graph.implementations.SingleGraph;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] arguments) throws InterruptedException {
        System.setProperty("org.graphstream.ui", "swing");
        var graph = new SingleGraph("I can see dead pixels");
        graph.display();

        var graphModel = new GraphModel(graph, "/media/sf_SharedFolder/szhor/szhor-simulator/src/main/resources/maze4");
        var edgeId = 0;

        for (var node : graphModel.getNodes()) {
            graph.addNode("" + node.getId());
            graph.getNode(node.getId()).setAttribute("ui.label", node.getId() + "(" + node.getSettledRobot() + "," + node.getMobileRobot() + ")");
            System.out.println("Node: " + node.getId());
            for (var neighbour : graphModel.getNeighbours(node)) {
                if (graph.getNode("" + neighbour.getId()) != null) {
                    System.out.println("Edge: " + node.getId() + " <-> " + neighbour.getId());
                    graph.addEdge("" + (edgeId++), "" + node.getId(), "" + neighbour.getId());
                }
            }
        }

        var sourceNode = graphModel.getSource();
        graph.getNode(sourceNode.getId()).setAttribute("ui.style", "fill-color: rgb(0,0,255); size: 20px; text-alignment: under;");

        var threads = new ArrayList<Thread>();
        while (!graphModel.isGraphFull()) {
            Thread.sleep((long) ThreadLocalRandom.current().nextInt(0, 2) * 1000);
            if (sourceNode.getMobileRobot() != null) {
                continue;
            }

            try {
                var robot = new Robot(graphModel, sourceNode, 0.1f);
                var thread = new Thread(robot);
                threads.add(thread);
                thread.start();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        for (var thread : threads) {
            thread.interrupt();
        }
        graphModel.refreshAll();
    }
}
