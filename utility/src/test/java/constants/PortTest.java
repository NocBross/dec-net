package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.Port;

public class PortTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        Port testClass = new Port();

        Assert.assertEquals(25712, Port.LOGIN_SERVICE);
        Assert.assertEquals(25713, Port.REGISTER_SERVICE);

        Assert.assertEquals(Port.LOGIN_SERVICE, testClass.LOGIN_SERVICE);
        Assert.assertEquals(Port.REGISTER_SERVICE, testClass.REGISTER_SERVICE);
    }

}
