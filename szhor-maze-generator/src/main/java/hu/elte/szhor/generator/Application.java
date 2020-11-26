package hu.elte.szhor.generator;

import hu.elte.szhor.core.util.ArgumentHandler;

import java.io.FileNotFoundException;

public class Application {

    public static void main(final String[] arguments) throws FileNotFoundException {
        ArgumentHandler.validate(arguments);

        final var maze = MazeGenerator.generate(ArgumentHandler.getSize());

        MazeGenerator.saveToFile(ArgumentHandler.getOutputFile(), maze);
    }
}
