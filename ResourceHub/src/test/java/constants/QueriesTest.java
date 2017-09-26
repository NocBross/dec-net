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
                "CREATE TABLE IF NOT EXISTS user_data ( nickname "
                        + "varchar(255) NOT NULL, password varchar(255) NOT NULL, PRIMARY KEY (nickname))",
                Queries.CREATE_USER_DATA);
        Assert.assertEquals("DELETE FROM user_data WHERE nickname=?", Queries.DELETE_USER);
        Assert.assertEquals("INSERT INTO user_data (nickname, password) VALUES (?, ?)", Queries.INSERT_USER);
        Assert.assertEquals("SELECT COUNT(*) AS 'numberOfRows', nickname, password FROM user_data WHERE nickname=?",
                Queries.LOGIN_QUERY);
        Assert.assertEquals("UPDATE user_data SET nickname=? WHERE nickname=?", Queries.UPDATE_NICKNAME);
        Assert.assertEquals("UPDATE user_data SET password=? WHERE nickname=?", Queries.UPDATE_PASSWORD);
        Assert.assertEquals("USE " + Database.NAME, Queries.USE_DATABASE);

        Assert.assertEquals(Queries.COUNT_NICKNAME, testClass.COUNT_NICKNAME);
        Assert.assertEquals(Queries.CREATE_DATABASE, testClass.CREATE_DATABASE);
        Assert.assertEquals(Queries.CREATE_USER_DATA, testClass.CREATE_USER_DATA);
        Assert.assertEquals(Queries.DELETE_USER, testClass.DELETE_USER);
        Assert.assertEquals(Queries.INSERT_USER, testClass.INSERT_USER);
        Assert.assertEquals(Queries.LOGIN_QUERY, testClass.LOGIN_QUERY);
        Assert.assertEquals(Queries.UPDATE_NICKNAME, testClass.UPDATE_NICKNAME);
        Assert.assertEquals(Queries.UPDATE_PASSWORD, testClass.UPDATE_PASSWORD);
        Assert.assertEquals(Queries.USE_DATABASE, testClass.USE_DATABASE);
    }

}
