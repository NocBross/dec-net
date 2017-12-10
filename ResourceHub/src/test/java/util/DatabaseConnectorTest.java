package test.java.util;

import java.sql.ResultSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.message.UpdateMessage;
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
            String resource = "/user/profile";
            List<String> resultList = null;
            DatabaseConnector connector = new DatabaseConnector(secrets.getDatabaseUser(),
                    secrets.getDatabasePassword());
            UpdateMessage message = new UpdateMessage();
            message.setResource(resource);
            message.setUserID(TestData.NICKNAME);

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

            // test search
            ResultSet result = connector.lookingForNickname(TestData.NICKNAME);
            Assert.assertTrue(result.next());
            Assert.assertEquals(TestData.NICKNAME, result.getString(1));
            Assert.assertFalse(result.next());
            result = connector.lookingForNickname("some_other_name");
            Assert.assertFalse(result.next());

            // test cache methods
            Assert.assertTrue(connector.insertCache(TestData.RESOURCE, TestData.NICKNAME));
            Assert.assertFalse(connector.insertCache(TestData.RESOURCE, TestData.NICKNAME));
            resultList = connector.readCache(TestData.RESOURCE);
            Assert.assertEquals(1, resultList.size());
            Assert.assertEquals(TestData.NICKNAME, resultList.get(0));
            Assert.assertTrue(connector.deleteCache(TestData.RESOURCE, TestData.NICKNAME));

            // test update message methods
            Assert.assertTrue(connector.insertUpdateMessage(TestData.NICKNAME, message.getMessage()));
            Assert.assertFalse(connector.insertUpdateMessage(TestData.NICKNAME, message.getMessage()));
            resultList = connector.readUpdateMessages(TestData.NICKNAME);
            Assert.assertEquals(1, resultList.size());
            Assert.assertEquals(message.getMessage(), resultList.get(0));
            Assert.assertTrue(connector.deleteUpdateMessage(TestData.NICKNAME, message.getMessage()));

            // test deleteUser
            Assert.assertTrue(connector.deleteUser("some_other_name"));
            Assert.assertTrue(connector.deleteUser(TestData.NICKNAME));
            Assert.assertFalse(connector.loginQuery(TestData.NICKNAME, TestData.PASSWORD));

            // test close operations
            connector.closeConnection();
            Assert.assertFalse(connector.insertNewUser(TestData.NICKNAME, TestData.PASSWORD));
            Assert.assertFalse(connector.loginQuery(TestData.NICKNAME, TestData.PASSWORD));
            Assert.assertFalse(connector.deleteUser(TestData.NICKNAME));
            Assert.assertFalse(connector.deleteCache(TestData.RESOURCE, TestData.NICKNAME));
            Assert.assertFalse(connector.deleteUpdateMessage(TestData.NICKNAME, message.getMessage()));
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
