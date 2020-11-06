package hu.elte.szhor.controller.util;

import hu.elte.szhor.model.MazeGraph;
import hu.elte.szhor.model.Robot;
import hu.elte.szhor.utils.Statistics;
import hu.elte.szhor.view.MazeGraphDisplay;
import java.util.LinkedList;

public class RobotController {

    private final static LinkedList<Thread> threads = new LinkedList<>();

    public static void startNewRobot(final MazeGraph model, final MazeGraphDisplay display, final int chanceOfCrash) {
        try {
            var robot = new Robot(model, display, chanceOfCrash);
            var thread = new Thread(robot);
            threads.add(thread);
            thread.start();
            Statistics.numberOfRobots++;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void interruptAllRobots() {
        for (var thread : threads) {
            thread.interrupt();
        }
    }
}
