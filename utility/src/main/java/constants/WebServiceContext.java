package main.java.constants;

public class WebServiceContext {

    /**
     * Defines the maximum time in seconds to wait until exchanges have finished.
     */
    public static final int STOP_DELAY = 0;

    /**
     * Defines the context which is used to connect to users.
     */
    public static final String CONNECTION = "/connection";

    /**
     * Defines the context which is used to register a new user.
     */
    public static final String REGISTER = "/register";

    /**
     * Defines the context which is used to handle resources.
     */
    public static final String RESOURCE = "/";;

    /**
     * Defines the context which used to find new people.
     */
    public static final String SEARCH = "/search";

    /**
     * Defines the contest which used to notify other users if a resource has
     * changed.
     */
    public static final String UPDATE = "/update";

}
