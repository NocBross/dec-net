package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.SecretKeys;

public class SecretKeysTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        SecretKeys testClass = new SecretKeys();

        Assert.assertEquals('#', SecretKeys.COMMENT_KEY);
        Assert.assertEquals("user", SecretKeys.DATABASE_USER);
        Assert.assertEquals("password", SecretKeys.DATABASE_PASSWORD);
        Assert.assertEquals("=", SecretKeys.KEY_VALUE_SEPARATOR);

        Assert.assertEquals(SecretKeys.COMMENT_KEY, testClass.COMMENT_KEY);
        Assert.assertEquals(SecretKeys.DATABASE_USER, testClass.DATABASE_USER);
        Assert.assertEquals(SecretKeys.DATABASE_PASSWORD, testClass.DATABASE_PASSWORD);
        Assert.assertEquals(SecretKeys.KEY_VALUE_SEPARATOR, testClass.KEY_VALUE_SEPARATOR);
    }

}
