package hu.elte.szhor.utils;

public class ArgumentHandler {

    private static String filename;
    private static int chanceOfCrash;

    public static void validate(final String[] arguments) throws NumberFormatException {
        var i = 0;
        while (i < arguments.length) {
            switch (arguments[i]) {
                case "--maze" -> filename = arguments[++i];
                case "--chance-of-crash" -> chanceOfCrash = (int) (100 * Float.parseFloat(arguments[++i]));
                default -> throw new IllegalArgumentException("Unknown argument: " + arguments[i] + ".");
            }
            ++i;
        }
    }

    public static String getFilename() {
        return filename;
    }

    public static int getChanceOfCrash() {
        return chanceOfCrash;
    }
}
