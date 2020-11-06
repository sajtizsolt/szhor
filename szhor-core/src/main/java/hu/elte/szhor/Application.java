package hu.elte.szhor;

import hu.elte.szhor.util.GraphBuilderHelper;

import java.util.*;

public class Application {

    public static void main(String[] arguments) throws InterruptedException {
        var graph = GraphBuilderHelper.buildEmptySimpleGraph();
        var sourceVertex = GraphBuilderHelper.fillGraph(graph, 3, 100);
        var threads = new ArrayList<Thread>();
        while (true) {
            Thread.sleep(1000);
            if (sourceVertex.getMobileRobot() != null) {
                continue;
            }

            var robot = new Robot(sourceVertex, 0.2f);
            var thread = new Thread(robot);
            threads.add(thread);
            thread.start();

            var occupiedVertices = 0;
            for (var vertex : graph.vertexSet()) {
                if (vertex.getSettledRobot() != null) {
                    occupiedVertices++;
                }
            }
            if (occupiedVertices == (graph.vertexSet().size() - 1)) {
                break;
            }
        }
        for (var thread : threads) {
            thread.interrupt();
        }
    }
}
