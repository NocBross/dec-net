package test.java.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.util.DatabaseConnector;
import main.java.util.ServerSecrets;
import test.java.TestData;

public class DatabaseConnectorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test() {
        ServerSecrets secrets = new ServerSecrets();
        try {
            secrets.loadServerSecrets();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }

        testMethods(secrets);
        testInjections(secrets);
    }

    /**
     * Tests the methods of the database connector without injections.
     */
    private void testMethods(ServerSecrets secrets) {
        try {
            DatabaseConnector connector = new DatabaseConnector(secrets.getDatabaseUser(),
                    secrets.getDatabasePassword());

            // test insertNewUser
            Assert.assertTrue(connector.insertNewUser(TestData.NICKNAME, TestData.PASSWORD));
            Assert.assertFalse(connector.insertNewUser(TestData.NICKNAME, TestData.PASSWORD));

            // test loginQuery
            Assert.assertTrue(connector.loginQuery(TestData.NICKNAME, TestData.PASSWORD));
            Assert.assertFalse(connector.loginQuery("some_other_name", TestData.PASSWORD));
            Assert.assertFalse(connector.loginQuery(TestData.NICKNAME, "some_wrong_password123!"));

            // test password update
            Assert.assertTrue(connector.passwordUpdate(TestData.NICKNAME, "123-Test.."));
            Assert.assertTrue(connector.passwordUpdate("some_other_name", "123-Test.."));
            Assert.assertFalse(connector.loginQuery("some_other_name", "123-Test.."));

            // test deleteUser
            Assert.assertTrue(connector.deleteUser("some_other_name"));
            Assert.assertTrue(connector.deleteUser(TestData.NICKNAME));
            Assert.assertFalse(connector.loginQuery(TestData.NICKNAME, TestData.PASSWORD));

            // test close operations
            connector.closeConnection();
            Assert.assertFalse(connector.insertNewUser(TestData.NICKNAME, TestData.PASSWORD));
            Assert.assertFalse(connector.loginQuery(TestData.NICKNAME, TestData.PASSWORD));
            Assert.assertFalse(connector.deleteUser(TestData.NICKNAME));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    /**
     * Tests the possibility of sql injections
     */
    private void testInjections(ServerSecrets secrets) {
        DatabaseConnector connector = new DatabaseConnector(secrets.getDatabaseUser(), secrets.getDatabasePassword());

        Assert.assertTrue(connector.insertNewUser(TestData.NICKNAME, TestData.PASSWORD));
        Assert.assertFalse(connector.loginQuery(TestData.NICKNAME + ";DELETE FROM user_data", TestData.PASSWORD));
        Assert.assertTrue(connector.loginQuery(TestData.NICKNAME, TestData.PASSWORD));
        Assert.assertTrue(connector.deleteUser(TestData.NICKNAME));

        connector.closeConnection();
    }

}
