package hu.elte.szhor.visualizer;

import hu.elte.szhor.core.model.util.GraphBuilder;
import hu.elte.szhor.visualizer.controller.MazeGraphController;
import hu.elte.szhor.core.util.ArgumentHandler;
import hu.elte.szhor.visualizer.view.MazeGraphDisplay;

import java.io.IOException;

public class Application {

    public static void main(String[] arguments) throws IOException, InterruptedException {
        ArgumentHandler.validate(arguments);

        final var model = GraphBuilder.fromFile(ArgumentHandler.getInputFile(), ArgumentHandler.getNumberOfSources());
        final var view = new MazeGraphDisplay();
        final var controller = new MazeGraphController(model, view);

        controller.startSimulation();
    }
}
