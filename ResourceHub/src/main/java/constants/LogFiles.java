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
     * Name of the log file for the ResourceService.
     */
    public static final String RESOURCE_LOG = LOG_PREFIX + "resource" + LOG_POSTFIX;

    /**
     * Name of the log file for the SearchService.
     */
    public static final String SEARCH_LOG = LOG_PREFIX + "search" + LOG_POSTFIX;

    /**
     * Name of the log file for the ShippingService.
     */
    public static final String SHIPPING_LOG = LOG_PREFIX + "shipping" + LOG_POSTFIX;

    /**
     * Name of the log file for the SUpdateService.
     */
    public static final String UPDATE_LOG = LOG_PREFIX + "update" + LOG_POSTFIX;

    /**
     * Name of the log file for Webservice.
     */
    public static final String WEBSERVICE_LOG = LOG_PREFIX + "webservice" + LOG_POSTFIX;
}
