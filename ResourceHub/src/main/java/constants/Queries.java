package main.java.constants;

public class Queries {

    /**
     * Counts all entries with the given mail address in the user_data table.
     */
    public static final String COUNT_NICKNAME = "SELECT COUNT(*) FROM user_data WHERE mail=?";

    /**
     * Creates a new database with the given name if no one exists.
     */
    public static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS " + Database.NAME;

    /**
     * Creates the resource_cache table if no one exists.
     */
    public static final String CREATE_RESOURCE_CACHE = "CREATE TABLE IF NOT EXISTS resource_cache (resource TEXT NOT NULL, store varchar(255) NOT NULL, "
            + "FOREIGN KEY (store) REFERENCES user_data(nickname) ON UPDATE CASCADE ON DELETE CASCADE)";

    /**
     * Creates the update_stream table of no exists.
     */
    public static final String CREATE_UPDATE_STREAM = "CREATE TABLE IF NOT EXISTS update_stream (nickname varchar(255) NOT NULL, message TEXT NOT NULL, "
            + " FOREIGN KEY (nickname) REFERENCES user_data(nickname) ON UPDATE CASCADE ON DELETE CASCADE)";

    /**
     * Creates the user_data table if no one exists.
     */
    public static final String CREATE_USER_DATA = "CREATE TABLE IF NOT EXISTS user_data ( nickname varchar(255) NOT NULL, password varchar(255) NOT NULL, "
            + "PRIMARY KEY (nickname))";

    /**
     * Deletes a specific resource.
     */
    public static final String DELETE_CACHE = "DELETE FROM resource_cache WHERE resource=? AND store=?";

    /**
     * Deletes a specific update message.
     */
    public static final String DELETE_UPDATE_MESSAGE = "DELETE FROM update_stream WHERE nickname=? AND message=?";

    /**
     * Deletes all entries in all tables with the given mail address.
     */
    public static final String DELETE_USER = "DELETE FROM user_data WHERE nickname=?";

    /**
     * Inserts a new cached resource into the resource_cache table.
     */
    public static final String INSERT_CACHE = "INSERT INTO resource_cache (resource, store) VALUES (?, ?)";

    /**
     * Inserts a new update message into the update_stream table.
     */
    public static final String INSERT_UPDATE_MESSAGE = "INSERT INTO update_stream (nickname, message) VALUES (?, ?)";

    /**
     * Inserts a new user into the user_data table.
     */
    public static final String INSERT_USER = "INSERT INTO user_data (nickname, password) VALUES (?, ?)";

    /**
     * Counts all entries in the user_data table with the given mail. Should always
     * be one.
     */
    public static final String LOGIN_QUERY = "SELECT COUNT(*) AS 'numberOfRows', nickname, password FROM user_data WHERE nickname=?";

    /**
     * Selects all temporary stores for one resource.
     */
    public static final String READ_CACHE = "SELECT store FROM resource_cache WHERE resource=?";

    /**
     * Selects all messages for a specific userID.
     */
    public static final String READ_UPDATE_MESSAGES = "SELECT message FROM update_stream WHERE nickname=?";

    /**
     * Replaced the old mail address with the new one.
     */
    public static final String UPDATE_NICKNAME = "UPDATE user_data SET nickname=? WHERE nickname=?";

    /**
     * Replaces the old password with the new one in the user_data table.
     */
    public static final String UPDATE_PASSWORD = "UPDATE user_data SET password=? WHERE nickname=?";

    /**
     * Sets the given database active to execute queries at there tables.
     */
    public static final String USE_DATABASE = "USE " + Database.NAME;

    /**
     * Looks for a specific nickname.
     */
    public static final String SEARCH = "SELECT nickname FROM user_data WHERE nickname LIKE CONCAT('%', ?, '%')";
}
