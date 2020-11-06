package hu.elte.szhor.utils;

public class ArgumentHandler {

    private static String inputFile;
    private static String outputFile;
    private static int chanceOfCrash;

    public static void validate(final String[] arguments) throws NumberFormatException {
        var i = 0;
        while (i < arguments.length) {
            switch (arguments[i]) {
                case "--input" -> inputFile = arguments[++i];
                case "--output" -> outputFile = arguments[++i];
                case "--chance-of-crash" -> chanceOfCrash = (int) (100 * Float.parseFloat(arguments[++i]));
                default -> throw new IllegalArgumentException("Unknown argument: " + arguments[i] + ".");
            }
            ++i;
        }
    }

    public static String getInputFile() {
        return inputFile;
    }

    public static String getOutputFile() {
        return outputFile;
    }

    public static int getChanceOfCrash() {
        return chanceOfCrash;
    }
}
