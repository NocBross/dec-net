package main.java.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;

import main.java.constants.Database;
import main.java.constants.Queries;

public class DatabaseConnector {

    private Connection connection;

    private PreparedStatement countNicknameStatement;
    private PreparedStatement deleteUserStatement;
    private PreparedStatement insertUserStatement;
    private PreparedStatement loginStatement;
    private PreparedStatement updateNicknameStatement;
    private PreparedStatement updatePasswordStatement;
    private PreparedStatement searchStatement;

    public DatabaseConnector(String databaseUser, String databasePassword) {
        try {
            initDatabase(databaseUser, databasePassword);

            Class.forName(Database.JDBC_DRIVER);
            connection = DriverManager.getConnection(Database.DB_URL, databaseUser, databasePassword);
            deleteUserStatement = connection.prepareStatement(Queries.DELETE_USER);
            countNicknameStatement = connection.prepareStatement(Queries.COUNT_NICKNAME);
            insertUserStatement = connection.prepareStatement(Queries.INSERT_USER);
            loginStatement = connection.prepareStatement(Queries.LOGIN_QUERY);
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
            deleteUserStatement.close();
            countNicknameStatement.close();
            insertUserStatement.close();
            loginStatement.close();
            updateNicknameStatement.close();
            updatePasswordStatement.close();
            searchStatement.close();
            connection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
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

        statement.close();
        connection.close();
    }
}
