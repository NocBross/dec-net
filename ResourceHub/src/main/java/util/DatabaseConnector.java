package main.java.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import main.java.constants.Database;
import main.java.constants.Queries;

public class DatabaseConnector {

    private Connection connection;

    private PreparedStatement countNicknameStatement;
    private PreparedStatement deleteCacheStatament;
    private PreparedStatement deleteUpdateMessageStatament;
    private PreparedStatement deleteUserStatement;
    private PreparedStatement insertCacheStatement;
    private PreparedStatement insertUpdateMessageStatement;
    private PreparedStatement insertUserStatement;
    private PreparedStatement loginStatement;
    private PreparedStatement readCacheStatement;
    private PreparedStatement readUpdateMessageStatement;
    private PreparedStatement updateNicknameStatement;
    private PreparedStatement updatePasswordStatement;
    private PreparedStatement searchStatement;

    public DatabaseConnector(String databaseUser, String databasePassword) {
        try {
            initDatabase(databaseUser, databasePassword);

            Class.forName(Database.JDBC_DRIVER);
            connection = DriverManager.getConnection(Database.DB_URL, databaseUser, databasePassword);
            deleteCacheStatament = connection.prepareStatement(Queries.DELETE_CACHE);
            deleteUpdateMessageStatament = connection.prepareStatement(Queries.DELETE_UPDATE_MESSAGE);
            deleteUserStatement = connection.prepareStatement(Queries.DELETE_USER);
            countNicknameStatement = connection.prepareStatement(Queries.COUNT_NICKNAME);
            insertCacheStatement = connection.prepareStatement(Queries.INSERT_CACHE);
            insertUpdateMessageStatement = connection.prepareStatement(Queries.INSERT_UPDATE_MESSAGE);
            insertUserStatement = connection.prepareStatement(Queries.INSERT_USER);
            loginStatement = connection.prepareStatement(Queries.LOGIN_QUERY);
            readCacheStatement = connection.prepareStatement(Queries.READ_CACHE);
            readUpdateMessageStatement = connection.prepareStatement(Queries.READ_UPDATE_MESSAGES);
            updateNicknameStatement = connection.prepareStatement(Queries.UPDATE_NICKNAME);
            updatePasswordStatement = connection.prepareStatement(Queries.UPDATE_PASSWORD);
            searchStatement = connection.prepareStatement(Queries.SEARCH);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            deleteCacheStatament.close();
            deleteUpdateMessageStatament.close();
            deleteUserStatement.close();
            countNicknameStatement.close();
            insertCacheStatement.close();
            insertUpdateMessageStatement.close();
            insertUserStatement.close();
            loginStatement.close();
            readCacheStatement.close();
            readUpdateMessageStatement.close();
            updateNicknameStatement.close();
            updatePasswordStatement.close();
            searchStatement.close();
            connection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public boolean deleteCache(String resource, String store) {
        try {
            deleteCacheStatament.setString(1, resource);
            deleteCacheStatament.setString(2, store);
            deleteCacheStatament.execute();

            return true;
        } catch (SQLException sqle) {
        }

        return false;
    }

    public boolean deleteUpdateMessage(String userID, String message) {
        try {
            deleteUpdateMessageStatament.setString(1, userID);
            deleteUpdateMessageStatament.setString(2, message);
            deleteUpdateMessageStatament.execute();

            return true;
        } catch (SQLException sqle) {
        }

        return false;
    }

    /**
     * Deletes a user from the database.
     * 
     * @param nickname
     *            - mail of the user (hashed or not)
     * @return true in case of success; false otherwise
     */
    public boolean deleteUser(String nickname) {
        try {
            deleteUserStatement.setString(1, nickname);
            deleteUserStatement.execute();

            return true;
        } catch (SQLException sqle) {
        }

        return false;
    }

    public boolean insertCache(String resource, String store) {
        try {
            List<String> existingCache = readCache(resource);
            if (!existingCache.contains(store)) {
                insertCacheStatement.setString(1, resource);
                insertCacheStatement.setString(2, store);
                insertCacheStatement.execute();

                return true;
            }
        } catch (SQLException sqle) {
        }

        return false;
    }

    public boolean insertUpdateMessage(String userID, String message) {
        try {
            List<String> existingCache = readUpdateMessages(userID);
            if (!existingCache.contains(message)) {
                insertUpdateMessageStatement.setString(1, userID);
                insertUpdateMessageStatement.setString(2, message);
                insertUpdateMessageStatement.execute();

                return true;
            }
        } catch (SQLException sqle) {
        }

        return false;
    }

    /**
     * Inserts a new user into the database.
     * 
     * @param nickname
     *            - mail of the user
     * @param password
     *            - password which selected the user
     * @return true in case of success, false otherwise
     */
    public boolean insertNewUser(String nickname, String password) {
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            insertUserStatement.setString(1, nickname);
            insertUserStatement.setString(2, hashedPassword);
            insertUserStatement.execute();

            return true;
        } catch (SQLException sqle) {
        }

        return false;
    }

    /**
     * Checks if the given nickname exists and returns the ResultSet.
     * 
     * @param nickname
     *            - nickname of the user which is looking for
     * @return ResultSet with all matches
     */
    public ResultSet lookingForNickname(String nickname) {
        try {
            searchStatement.setString(1, nickname);
            return searchStatement.executeQuery();
        } catch (SQLException sqle) {
        }

        return null;
    }

    /**
     * Checks if the given mail-password-pair matches the stored data.
     * 
     * @param nickname
     *            - nickname of the user
     * @param password
     *            - password of the user
     * @return true in case of match, false otherwise
     */
    public boolean loginQuery(String nickname, String password) {
        try {
            loginStatement.setString(1, nickname);

            ResultSet rs = loginStatement.executeQuery();
            rs.next();
            if (rs.getInt("numberOfRows") == 1) {
                if (nickname.equals(rs.getString("nickname")) && BCrypt.checkpw(password, rs.getString("password"))) {
                    return true;
                }
            }
        } catch (SQLException sqle) {
        }

        return false;
    }

    /**
     * Sets the password of the given user to the new one
     * 
     * @param nickname
     *            - mail address of the user
     * @param newPassword
     *            - new user password
     * @return true in case of successfully changed, false otherwise
     */
    public boolean passwordUpdate(String nickname, String newPassword) {
        try {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            updatePasswordStatement.setString(1, nickname);
            updatePasswordStatement.setString(2, hashedPassword);
            updatePasswordStatement.execute();

            return true;
        } catch (SQLException sqle) {
        }

        return false;
    }

    public List<String> readCache(String resource) {
        List<String> cache = new LinkedList<String>();

        try {
            readCacheStatement.setString(1, resource);
            ResultSet rs = readCacheStatement.executeQuery();
            while (rs.next()) {
                cache.add(rs.getString("store"));
            }
        } catch (SQLException sqle) {
        }

        return cache;
    }

    public List<String> readUpdateMessages(String userID) {
        List<String> cache = new LinkedList<String>();

        try {
            readUpdateMessageStatement.setString(1, userID);
            ResultSet rs = readUpdateMessageStatement.executeQuery();
            while (rs.next()) {
                cache.add(rs.getString("message"));
            }
        } catch (SQLException sqle) {
        }

        return cache;
    }

    /**
     * Initializes the database.
     */
    private void initDatabase(String databaseUser, String databasePassword)
            throws ClassNotFoundException, SQLException {
        Connection connection;
        Statement statement;

        Class.forName(Database.JDBC_DRIVER);
        connection = DriverManager.getConnection(Database.DB_URL_INIT, databaseUser, databasePassword);
        statement = connection.createStatement();

        statement.execute(Queries.CREATE_DATABASE);
        statement.execute(Queries.USE_DATABASE);
        statement.execute(Queries.CREATE_USER_DATA);
        statement.execute(Queries.CREATE_RESOURCE_CACHE);
        statement.execute(Queries.CREATE_UPDATE_STREAM);

        statement.close();
        connection.close();
    }
}
