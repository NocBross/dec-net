package main.java.constants;

public class Network {

    /**
     * Used network protocol for URL.
     */
    public static final String NETWORK_PROTOCOL = "http://";

    /**
     * Address of the localhost.
     */
    public static final String LOCALHOST_ADDRESS = "localhost";

    /**
     * Port on which the client web server is listen.
     */
    public static final int CLIENT_WEBSERVER_PORT = 25000;

    /**
     * Port on which the login service is listen.
     */
    public static final int LOGIN_SERVICE_PORT = 25712;

    /**
     * Port on which the register service is listen.
     */
    public static final int REGISTER_SERVICE_PORT = 25713;

    /**
     * Port on which the WebService is listen.
     */
    public static final int WEBSERVICE_PORT = 25700;

    /**
     * URL of the localhost.
     */
    public static final String LOCALHOST_URL = NETWORK_PROTOCOL + LOCALHOST_ADDRESS + ":" + CLIENT_WEBSERVER_PORT;

    /**
     * URL of the ResourceHub.
     */
    public static final String RESOURCE_HUB_URL = NETWORK_PROTOCOL + LOCALHOST_ADDRESS + ":" + WEBSERVICE_PORT;
}
