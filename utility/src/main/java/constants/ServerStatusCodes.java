package main.java.constants;

public class ServerStatusCodes {

    /**
     * Defines the status code which is send if the login was successful.
     */
    public static final int LOGIN_CORRECT = 1000;

    /**
     * Defines the status code which is send if the user already connected.
     */
    public static final int LOGIN_OPEN_CONNECTION = 1001;

    /**
     * Defines the status code which is send if the userID or password is wrong or
     * not registered.
     */
    public static final int LOGIN_UNKNOWN_USER_ID = 2002;

    /**
     * Defines the status code which is send if the registration was successful.
     */
    public static final int REGISTER_CORRECT = 2000;

    /**
     * Defines the status code which is send if the userID already registered.
     */
    public static final int REGISTER_KNOWN_USER_ID = 2001;

    /**
     * Defines the statuscode which is used if the wrong message was send.
     */
    public static final int WRONG_MESSAGE_TYPE = 0;

}
