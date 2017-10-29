package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.WebServiceConstants;

public class WebServiceConstantsTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        WebServiceConstants testWebServiceConstants = new WebServiceConstants();

        Assert.assertEquals("\\?", WebServiceConstants.SEARCH_CONTEXT_SEPARATOR);
        Assert.assertEquals("?", WebServiceConstants.SEARCH_SEPARATOR);
        Assert.assertEquals("=", WebServiceConstants.KEY_VALUE_SEPARATOR);
        Assert.assertEquals("nickname", WebServiceConstants.SEARCH_NICKNAME_KEY);

        Assert.assertEquals(WebServiceConstants.SEARCH_CONTEXT_SEPARATOR,
                testWebServiceConstants.SEARCH_CONTEXT_SEPARATOR);
        Assert.assertEquals(WebServiceConstants.SEARCH_SEPARATOR, testWebServiceConstants.SEARCH_SEPARATOR);
        Assert.assertEquals(WebServiceConstants.KEY_VALUE_SEPARATOR, testWebServiceConstants.KEY_VALUE_SEPARATOR);
        Assert.assertEquals(WebServiceConstants.SEARCH_NICKNAME_KEY, testWebServiceConstants.SEARCH_NICKNAME_KEY);
    }

}
