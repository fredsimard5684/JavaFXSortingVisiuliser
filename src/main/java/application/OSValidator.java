package application;

public class OSValidator {
    private final static String OPERATINGSYSTEM = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return OPERATINGSYSTEM.contains("win");
    }

    public static boolean isMac() {
        return OPERATINGSYSTEM.contains("mac");
    }

    public static boolean isLinux() {
        return OPERATINGSYSTEM.contains("nux");
    }
}
