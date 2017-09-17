package test.java.abstraction;

import org.junit.Assert;
import org.junit.Test;

import main.java.client_agent.abstraction.UserModel;
import test.java.TestData;

public class UserModelTest {

    @Test
    public void test() {
        UserModel data = new UserModel();

        // test initialize values
        Assert.assertEquals("", data.getNickname());
        Assert.assertEquals("", data.getPassword());

        // test mail
        data.setNickname(TestData.NICHNAME_1);
        Assert.assertEquals(TestData.NICHNAME_1, data.getNickname());
        data.setNickname(TestData.NICKNAME_2);
        Assert.assertEquals(TestData.NICKNAME_2, data.getNickname());

        // test password
        data.setPassword(TestData.PASSWORD);
        Assert.assertEquals(TestData.PASSWORD, data.getPassword());
        data.setPassword(TestData.PASSWORD + "123");
        Assert.assertEquals(TestData.PASSWORD + "123", data.getPassword());
    }
}
