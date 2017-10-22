package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.Network;

public class PortTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        Network testClass = new Network();

        Assert.assertEquals(25712, Network.LOGIN_SERVICE_PORT);
        Assert.assertEquals(25713, Network.REGISTER_SERVICE_PORT);

        Assert.assertEquals(Network.LOGIN_SERVICE_PORT, testClass.LOGIN_SERVICE_PORT);
        Assert.assertEquals(Network.REGISTER_SERVICE_PORT, testClass.REGISTER_SERVICE_PORT);
    }

}
