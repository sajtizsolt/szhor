package hu.elte.szhor;

import hu.elte.szhor.controller.MazeGraphController;
import hu.elte.szhor.model.util.MazeGraphBuilder;
import hu.elte.szhor.utils.ArgumentHandler;
import hu.elte.szhor.utils.Statistics;
import hu.elte.szhor.view.MazeGraphDisplay;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] arguments) throws InterruptedException, IOException {
        // Validate cmd arguments
        ArgumentHandler.validate(arguments);

        // Create model
        var model = MazeGraphBuilder.fromFile(ArgumentHandler.getInputFile());

        // Create display
        var view = new MazeGraphDisplay();

        // Create controller
        var controller = new MazeGraphController(model, view);

        // Simulate with display
        controller.startSimulation();
        controller.stopSimulation();

        // Save statistics
        try (var writer = new PrintWriter(ArgumentHandler.getOutputFile())) {
            writer.write(Statistics.getJsonString());
            writer.flush();
        }
    }
}
