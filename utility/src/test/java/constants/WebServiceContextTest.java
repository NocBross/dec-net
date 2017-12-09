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
        Assert.assertEquals("/connection", WebServiceContext.CONNECTION);
        Assert.assertEquals("/register", WebServiceContext.REGISTER);
        Assert.assertEquals("/", WebServiceContext.RESOURCE);
        Assert.assertEquals("/search", WebServiceContext.SEARCH);

        Assert.assertEquals(WebServiceContext.STOP_DELAY, testWebServiceContext.STOP_DELAY);
        Assert.assertEquals(WebServiceContext.CONNECTION, testWebServiceContext.CONNECTION);
        Assert.assertEquals(WebServiceContext.REGISTER, testWebServiceContext.REGISTER);
        Assert.assertEquals(WebServiceContext.RESOURCE, testWebServiceContext.RESOURCE);
        Assert.assertEquals(WebServiceContext.SEARCH, testWebServiceContext.SEARCH);
    }

}
