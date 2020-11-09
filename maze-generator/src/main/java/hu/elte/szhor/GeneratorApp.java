package hu.elte.szhor;

import hu.elte.szhor.model.MazeGenerator;
import hu.elte.szhor.util.ArgumentHandler;

public class GeneratorApp {

    public static void main(String[] args){
        ArgumentHandler.validate(args);

        var generator = new MazeGenerator(ArgumentHandler.getRows(), ArgumentHandler.getColumns(), ArgumentHandler.getInputFile());
        generator.generateMaze();
    }
}
