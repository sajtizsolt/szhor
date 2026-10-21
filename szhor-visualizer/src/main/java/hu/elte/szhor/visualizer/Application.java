package hu.elte.szhor.visualizer;

import hu.elte.szhor.core.model.util.GraphBuilder;
import hu.elte.szhor.core.util.ArgumentHandler;
import hu.elte.szhor.visualizer.controller.MazeGraphController;
import hu.elte.szhor.visualizer.view.MazeGraphDisplay;

import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class Application {

    public static void main(final String[] arguments) throws IOException, InterruptedException {
        if (ArgumentHandler.hasHelpFlag(arguments)) {
            ArgumentHandler.printVisualizerHelp();
            return;
        }

        final var config = ArgumentHandler.parseVisualizerConfig(arguments);

        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Error: Graphical visualizer cannot run in a headless environment.");
            System.exit(1);
        }

        final var model = GraphBuilder.fromFile(config.inputFile(), config.numberOfSources());
        final var view = new MazeGraphDisplay();
        final var controller = new MazeGraphController(model, view, config.threadWait(), config.chanceOfCrash());

        controller.startSimulation();
    }
}
