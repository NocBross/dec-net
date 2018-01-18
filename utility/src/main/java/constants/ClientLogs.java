package main.java.constants;

public class ClientLogs {

    public static final String LOG_PARENT_DIR = System.getProperty("user.dir") + "/logs/";

    public static final String LOG_PREFIX = "log_";

    public static final String LOG_POSTFIX = ".txt";

    public static final String AUTHENTICATION_AGENT = LOG_PARENT_DIR + LOG_PREFIX + "authentication_agent"
            + LOG_POSTFIX;

    public static final String CLIENT_AGENT = LOG_PARENT_DIR + LOG_PREFIX + "client_agent" + LOG_POSTFIX;

    public static final String FRIENDSHIP_AGENT = LOG_PARENT_DIR + LOG_PREFIX + "friendship_agent" + LOG_POSTFIX;

    public static final String LOGIN_AGENT = LOG_PARENT_DIR + LOG_PREFIX + "login_agent" + LOG_POSTFIX;

    public static final String PINBOARD_AGENT = LOG_PARENT_DIR + LOG_PREFIX + "pinboard_agent" + LOG_POSTFIX;

    public static final String REGISTER_AGENT = LOG_PARENT_DIR + LOG_PREFIX + "register_agent" + LOG_POSTFIX;

    public static final String SOC_NET_AGENT = LOG_PARENT_DIR + LOG_PREFIX + "soc_net_agent" + LOG_POSTFIX;

}
