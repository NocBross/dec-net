package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.WebServiceConstants;

public class WebServiceConstantsTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        WebServiceConstants testWebServiceConstants = new WebServiceConstants();

        Assert.assertEquals("\\?", WebServiceConstants.CONTEXT_SEPARATOR_ESCAPED);
        Assert.assertEquals("?", WebServiceConstants.CONTEXT_SEPARATOR);
        Assert.assertEquals("=", WebServiceConstants.KEY_VALUE_SEPARATOR);
        Assert.assertEquals("nickname", WebServiceConstants.USER_ID_KEY);

        Assert.assertEquals(WebServiceConstants.CONTEXT_SEPARATOR_ESCAPED,
                testWebServiceConstants.CONTEXT_SEPARATOR_ESCAPED);
        Assert.assertEquals(WebServiceConstants.CONTEXT_SEPARATOR, testWebServiceConstants.CONTEXT_SEPARATOR);
        Assert.assertEquals(WebServiceConstants.KEY_VALUE_SEPARATOR, testWebServiceConstants.KEY_VALUE_SEPARATOR);
        Assert.assertEquals(WebServiceConstants.USER_ID_KEY, testWebServiceConstants.USER_ID_KEY);
    }

}
