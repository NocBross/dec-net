package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.WebServiceContext;

public class WebServiceContextTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        WebServiceContext testWebServiceContext = new WebServiceContext();

        Assert.assertEquals(0, WebServiceContext.STOP_DELAY);
        Assert.assertEquals("/register", WebServiceContext.REGISTER);
        Assert.assertEquals("/search", WebServiceContext.SEARCH);

        Assert.assertEquals(WebServiceContext.STOP_DELAY, testWebServiceContext.STOP_DELAY);
        Assert.assertEquals(WebServiceContext.REGISTER, testWebServiceContext.REGISTER);
        Assert.assertEquals(WebServiceContext.SEARCH, testWebServiceContext.SEARCH);
    }

}
