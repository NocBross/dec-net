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
     * Creates the user_data table if no one exists.
     */
    public static final String CREATE_USER_DATA = "CREATE TABLE IF NOT EXISTS user_data ( nickname varchar(255) NOT NULL, password varchar(255) NOT NULL, "
            + "PRIMARY KEY (nickname))";

    /**
     * Deletes all entries in all tables with the given mail address.
     */
    public static final String DELETE_USER = "DELETE FROM user_data WHERE nickname=?";

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
