package linguacrypt.utils;

import java.util.logging.Logger;

public class DataUtils {
    private static final Logger logger = Logger.getLogger(DataUtils.class.getName());

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void logException(Exception e, String message) {
        logger.log(java.util.logging.Level.SEVERE, message, e);
    }
}
