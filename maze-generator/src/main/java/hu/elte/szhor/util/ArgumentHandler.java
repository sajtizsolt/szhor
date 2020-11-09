package hu.elte.szhor.util;

public class ArgumentHandler {

    private static String inputFile;
    private static int rows;
    private static int columns;

    public static void validate(final String[] arguments) throws NumberFormatException {
        var i = 0;
        while (i < arguments.length) {
            switch (arguments[i]) {
                case "--rows" -> rows = Integer.parseInt(arguments[++i]);
                case "--columns" -> columns = Integer.parseInt(arguments[++i]);
                case "--input" -> inputFile = arguments[++i];
                default -> throw new IllegalArgumentException("Unknown argument: " + arguments[i] + ".");
            }
            ++i;
        }
    }

    public static String getInputFile() {
        return inputFile;
    }

    public static int getRows() {
        return rows;
    }

    public static int getColumns() {
        return columns;
    }
}
