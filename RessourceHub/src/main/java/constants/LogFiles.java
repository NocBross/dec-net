package main.java.constants;

public class LogFiles {

    /**
     * Prefix of all log files.
     */
    public static final String LOG_PREFIX = "logs/log_";

    /**
     * Post fix of all log files which defines the file type
     */
    public static final String LOG_POSTFIX = ".txt";

    /**
     * Name of the log file for the LoginService.
     */
    public static final String LOGIN_LOG = LOG_PREFIX + "login" + LOG_POSTFIX;

    /**
     * Name of the log file for the RegisterService.
     */
    public static final String REGISTER_LOG = LOG_PREFIX + "register" + LOG_POSTFIX;

    /**
     * Name of the log file for the ShippginService.
     */
    public static final String SHIPPING_LOG = LOG_PREFIX + "shipping" + LOG_POSTFIX;
}
