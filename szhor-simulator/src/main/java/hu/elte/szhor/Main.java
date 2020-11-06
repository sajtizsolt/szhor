package hu.elte.szhor;

import hu.elte.szhor.controller.MazeGraphController;
import hu.elte.szhor.model.util.MazeGraphBuilder;
import hu.elte.szhor.utils.ArgumentHandler;
import java.io.IOException;

public class Main {

    public static void main(String[] arguments) throws InterruptedException, IOException {
        // Validate cmd arguments
        ArgumentHandler.validate(arguments);

        // Create model
        var model = MazeGraphBuilder.fromFile(ArgumentHandler.getFilename());

        // Create controller
        var controller = new MazeGraphController(model);

        /*var threads = new ArrayList<Thread>();
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
        graphModel.refreshAll();*/
    }
}
