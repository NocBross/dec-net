package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.Database;

public class DatabaseTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        Database testClass = new Database();

        Assert.assertEquals("com.mysql.cj.jdbc.Driver", Database.JDBC_DRIVER);
        Assert.assertEquals("ResourceHub_data", Database.NAME);
        Assert.assertEquals("jdbc:mysql://localhost/" + Database.NAME
                + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&"
                + "serverTimezone=UTC&useSSL=false", Database.DB_URL);
        Assert.assertEquals("jdbc:mysql://localhost?useUnicode=true&useJDBCCompliantTimezoneShift=true&"
                + "useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", Database.DB_URL_INIT);
        Assert.assertEquals("secrets/database", Database.DATABASE_SECRET_PATH);

        Assert.assertEquals(Database.JDBC_DRIVER, testClass.JDBC_DRIVER);
        Assert.assertEquals(Database.NAME, testClass.NAME);
        Assert.assertEquals(Database.DB_URL, testClass.DB_URL);
        Assert.assertEquals(Database.DB_URL_INIT, testClass.DB_URL_INIT);
        Assert.assertEquals(Database.DATABASE_SECRET_PATH, testClass.DATABASE_SECRET_PATH);
    }

}
