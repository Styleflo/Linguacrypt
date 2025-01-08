package linguacrypt.utils;

import java.util.logging.Logger;

public class DataVerification {
    private static final Logger logger = Logger.getLogger(DataVerification.class.getName());

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void logException(Exception e, String message) {
        logger.log(java.util.logging.Level.SEVERE, message, e);
    }
}
