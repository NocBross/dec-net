package test.java.abstraction;

import org.junit.Assert;
import org.junit.Test;

import main.java.client_agent.abstraction.UserModel;
import test.java.TestData;

public class UserModelTest {

    @Test
    public void test() {
        String userID_1 = "test_user_1@localhost";
        String userID_2 = "test_user_2@localhost";
        UserModel data = new UserModel();

        // test initialize values
        Assert.assertEquals("", data.getNickname());
        Assert.assertEquals("", data.getPassword());
        Assert.assertEquals("", data.getResourceHubAddress());
        Assert.assertEquals("", data.getUserID());

        // test userID
        data.setUserID(userID_1);
        Assert.assertEquals(userID_1.split("@")[0], data.getNickname());
        Assert.assertEquals(userID_1.split("@")[1], data.getResourceHubAddress());
        Assert.assertEquals(userID_1, data.getUserID());
        data.setUserID(userID_2);
        Assert.assertEquals(userID_2.split("@")[0], data.getNickname());
        Assert.assertEquals(userID_2.split("@")[1], data.getResourceHubAddress());
        Assert.assertEquals(userID_2, data.getUserID());

        // test password
        data.setPassword(TestData.PASSWORD);
        Assert.assertEquals(TestData.PASSWORD, data.getPassword());
        data.setPassword(TestData.PASSWORD + "123");
        Assert.assertEquals(TestData.PASSWORD + "123", data.getPassword());
    }
}
