package test.java.constants;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.Security;

public class SecurityTest {

    @SuppressWarnings("static-access")
    @Test
    public void test() {
        Security testSecurity = new Security();

        Assert.assertEquals("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ%!#$*+.,-/:;=?@_<>",
                Security.PASSWORD_CHARACTERS);
        Assert.assertEquals(10, Security.MIN_PASSWORD_LENGTH);

        Assert.assertEquals(Security.PASSWORD_CHARACTERS, testSecurity.PASSWORD_CHARACTERS);
        Assert.assertEquals(Security.MIN_PASSWORD_LENGTH, testSecurity.MIN_PASSWORD_LENGTH);
    }

}
