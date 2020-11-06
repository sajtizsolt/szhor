package hu.elte.szhor;

import hu.elte.szhor.controller.MazeGraphController;
import hu.elte.szhor.model.util.MazeGraphBuilder;
import hu.elte.szhor.utils.ArgumentHandler;
import hu.elte.szhor.view.MazeGraphDisplay;
import java.io.IOException;

public class Main {

    public static void main(String[] arguments) throws InterruptedException, IOException {
        // Validate cmd arguments
        ArgumentHandler.validate(arguments);

        // Create model
        var model = MazeGraphBuilder.fromFile(ArgumentHandler.getFilename());

        // Create display
        var view = new MazeGraphDisplay();

        // Create controller
        var controller = new MazeGraphController(model, view);

        // Simulate with display
        controller.startSimulation();
        controller.stopSimulation();

        // Save statistics
    }
}
