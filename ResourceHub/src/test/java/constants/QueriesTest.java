package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.Database;
import main.java.constants.Queries;

public class QueriesTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        Queries testClass = new Queries();

        Assert.assertEquals("SELECT COUNT(*) FROM user_data WHERE mail=?", Queries.COUNT_NICKNAME);
        Assert.assertEquals("CREATE DATABASE IF NOT EXISTS " + Database.NAME, Queries.CREATE_DATABASE);
        Assert.assertEquals(
                "CREATE TABLE IF NOT EXISTS resource_cache (resource TEXT NOT NULL, store varchar(255) NOT NULL, "
                        + "FOREIGN KEY (store) REFERENCES user_data(nickname) ON UPDATE CASCADE ON DELETE CASCADE)",
                Queries.CREATE_RESOURCE_CACHE);
        Assert.assertEquals(
                "CREATE TABLE IF NOT EXISTS update_stream (nickname varchar(255) NOT NULL, message TEXT NOT NULL, "
                        + " FOREIGN KEY (nickname) REFERENCES user_data(nickname) ON UPDATE CASCADE ON DELETE CASCADE)",
                Queries.CREATE_UPDATE_STREAM);
        Assert.assertEquals(
                "CREATE TABLE IF NOT EXISTS user_data ( nickname "
                        + "varchar(255) NOT NULL, password varchar(255) NOT NULL, PRIMARY KEY (nickname))",
                Queries.CREATE_USER_DATA);
        Assert.assertEquals("DELETE FROM resource_cache WHERE resource=? AND store=?", Queries.DELETE_CACHE);
        Assert.assertEquals("DELETE FROM update_stream WHERE nickname=? AND message=?", Queries.DELETE_UPDATE_MESSAGE);
        Assert.assertEquals("DELETE FROM user_data WHERE nickname=?", Queries.DELETE_USER);
        Assert.assertEquals("INSERT INTO resource_cache (resource, store) VALUES (?, ?)", Queries.INSERT_CACHE);
        Assert.assertEquals("INSERT INTO update_stream (nickname, message) VALUES (?, ?)",
                Queries.INSERT_UPDATE_MESSAGE);
        Assert.assertEquals("INSERT INTO user_data (nickname, password) VALUES (?, ?)", Queries.INSERT_USER);
        Assert.assertEquals("SELECT COUNT(*) AS 'numberOfRows', nickname, password FROM user_data WHERE nickname=?",
                Queries.LOGIN_QUERY);
        Assert.assertEquals("SELECT store FROM resource_cache WHERE resource=?", Queries.READ_CACHE);
        Assert.assertEquals("SELECT message FROM update_stream WHERE nickname=?", Queries.READ_UPDATE_MESSAGES);
        Assert.assertEquals("UPDATE user_data SET nickname=? WHERE nickname=?", Queries.UPDATE_NICKNAME);
        Assert.assertEquals("UPDATE user_data SET password=? WHERE nickname=?", Queries.UPDATE_PASSWORD);
        Assert.assertEquals("USE " + Database.NAME, Queries.USE_DATABASE);
        Assert.assertEquals("SELECT nickname FROM user_data WHERE nickname LIKE CONCAT('%', ?, '%')", Queries.SEARCH);

        Assert.assertEquals(Queries.COUNT_NICKNAME, testClass.COUNT_NICKNAME);
        Assert.assertEquals(Queries.CREATE_DATABASE, testClass.CREATE_DATABASE);
        Assert.assertEquals(Queries.CREATE_RESOURCE_CACHE, testClass.CREATE_RESOURCE_CACHE);
        Assert.assertEquals(Queries.CREATE_UPDATE_STREAM, testClass.CREATE_UPDATE_STREAM);
        Assert.assertEquals(Queries.CREATE_USER_DATA, testClass.CREATE_USER_DATA);
        Assert.assertEquals(Queries.DELETE_CACHE, testClass.DELETE_CACHE);
        Assert.assertEquals(Queries.DELETE_UPDATE_MESSAGE, testClass.DELETE_UPDATE_MESSAGE);
        Assert.assertEquals(Queries.DELETE_USER, testClass.DELETE_USER);
        Assert.assertEquals(Queries.INSERT_CACHE, testClass.INSERT_CACHE);
        Assert.assertEquals(Queries.INSERT_UPDATE_MESSAGE, testClass.INSERT_UPDATE_MESSAGE);
        Assert.assertEquals(Queries.INSERT_USER, testClass.INSERT_USER);
        Assert.assertEquals(Queries.LOGIN_QUERY, testClass.LOGIN_QUERY);
        Assert.assertEquals(Queries.READ_CACHE, testClass.READ_CACHE);
        Assert.assertEquals(Queries.READ_UPDATE_MESSAGES, testClass.READ_UPDATE_MESSAGES);
        Assert.assertEquals(Queries.UPDATE_NICKNAME, testClass.UPDATE_NICKNAME);
        Assert.assertEquals(Queries.UPDATE_PASSWORD, testClass.UPDATE_PASSWORD);
        Assert.assertEquals(Queries.USE_DATABASE, testClass.USE_DATABASE);
        Assert.assertEquals(Queries.SEARCH, testClass.SEARCH);
    }

}
