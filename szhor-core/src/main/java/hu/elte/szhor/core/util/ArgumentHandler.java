package hu.elte.szhor.core.util;

public class ArgumentHandler {

    private static String inputFile;
    private static String outputFile;
    private static int numberOfSources;
    private static int chanceOfCrash;
    private static int threadWait = 100;

    public static void validate(final String[] arguments) throws IllegalArgumentException {
        var i = 0;
        while (i < arguments.length) {
            switch (arguments[i]) {
                case "--input" -> inputFile = arguments[++i];
                case "--output" -> outputFile = arguments[++i];
                case "--number-of-sources" -> numberOfSources = Integer.parseInt(arguments[++i]);
                case "--chance-of-crash" -> chanceOfCrash = (int) (100 * Float.parseFloat(arguments[++i]));
                case "--thread-wait" -> threadWait = Integer.parseInt(arguments[++i]);
                default -> throw new IllegalArgumentException("Unknown argument: " + arguments[i] + ".");
            }
            ++i;
        }
        Statistics.numberOfSources = numberOfSources;
        Statistics.chanceOfCrash = chanceOfCrash;
    }

    public static String getInputFile() {
        return inputFile;
    }

    public static String getOutputFile() {
        return outputFile;
    }

    public static int getNumberOfSources() {
        return numberOfSources;
    }

    public static int getChanceOfCrash() {
        return chanceOfCrash;
    }

    public static int getThreadWait() {
        return threadWait;
    }
}
