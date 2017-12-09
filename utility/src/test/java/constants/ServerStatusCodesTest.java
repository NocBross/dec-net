package test.java.constants;

import org.junit.Assert;
import org.junit.Test;
import main.java.constants.ServerStatusCodes;

public class ServerStatusCodesTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        ServerStatusCodes testStatus = new ServerStatusCodes();

        Assert.assertEquals(1000, ServerStatusCodes.LOGIN_CORRECT);
        Assert.assertEquals(1001, ServerStatusCodes.LOGIN_OPEN_CONNECTION);
        Assert.assertEquals(2002, ServerStatusCodes.LOGIN_UNKNOWN_USER_ID);
        Assert.assertEquals(2000, ServerStatusCodes.REGISTER_CORRECT);
        Assert.assertEquals(2001, ServerStatusCodes.REGISTER_KNOWN_USER_ID);
        Assert.assertEquals(0, ServerStatusCodes.WRONG_MESSAGE_TYPE);

        Assert.assertEquals(ServerStatusCodes.LOGIN_CORRECT, testStatus.LOGIN_CORRECT);
        Assert.assertEquals(ServerStatusCodes.LOGIN_OPEN_CONNECTION, testStatus.LOGIN_OPEN_CONNECTION);
        Assert.assertEquals(ServerStatusCodes.LOGIN_UNKNOWN_USER_ID, testStatus.LOGIN_UNKNOWN_USER_ID);
        Assert.assertEquals(ServerStatusCodes.REGISTER_CORRECT, testStatus.REGISTER_CORRECT);
        Assert.assertEquals(ServerStatusCodes.REGISTER_KNOWN_USER_ID, testStatus.REGISTER_KNOWN_USER_ID);
        Assert.assertEquals(ServerStatusCodes.WRONG_MESSAGE_TYPE, testStatus.WRONG_MESSAGE_TYPE);
    }

}
