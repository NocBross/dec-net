package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.EndPoint;

public class EndPointTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        EndPoint testClass = new EndPoint();

        Assert.assertEquals("service_", EndPoint.SERVICE_PREFIX);
        Assert.assertEquals(EndPoint.SERVICE_PREFIX + "login", EndPoint.LOGIN_END_POINT);
        Assert.assertEquals(EndPoint.SERVICE_PREFIX + "register", EndPoint.REGISTER_END_POINT);

        Assert.assertEquals(EndPoint.SERVICE_PREFIX, testClass.SERVICE_PREFIX);
        Assert.assertEquals(EndPoint.LOGIN_END_POINT, testClass.LOGIN_END_POINT);
        Assert.assertEquals(EndPoint.REGISTER_END_POINT, testClass.REGISTER_END_POINT);
    }

}
