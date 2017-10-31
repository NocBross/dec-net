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
     * Port on which the WebService of the server is listen.
     */
    public static final int SERVER_WEBSERVICE_PORT = 25700;

    /**
     * ResourceHub address of the LoginService and the ShippingService.
     */
    public static final String NETWORK_HUB = "hub_service";

    /**
     * Port on which the LoginService and the ShippingService is listen.
     */
    public static final int NETWORK_HUB_PORT = 25800;

    /**
     * URL of the localhost.
     */
    public static final String LOCALHOST_URL = NETWORK_PROTOCOL + LOCALHOST_ADDRESS + ":" + CLIENT_WEBSERVER_PORT;
}
