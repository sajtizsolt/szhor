package hu.elte.szhor.generator;

import hu.elte.szhor.core.util.ArgumentHandler;

import java.io.IOException;

public class Application {

    public static void main(final String[] arguments) throws IOException {
        if (ArgumentHandler.hasHelpFlag(arguments)) {
            ArgumentHandler.printGeneratorHelp();
            return;
        }

        final var config = ArgumentHandler.parseGeneratorConfig(arguments);
        final var maze = MazeGenerator.generate(config.size());

        MazeGenerator.saveToFile(config.outputFile(), maze);
    }
}
