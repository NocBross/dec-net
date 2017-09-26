package test.java.util;

import org.junit.Assert;
import org.junit.Test;

import main.java.util.ServerSecrets;

public class ServerSecretsTest {

    @Test
    public void test() {
        ServerSecrets secrets = new ServerSecrets();

        // test initial values
        Assert.assertEquals("", secrets.getDatabaseUser());
        Assert.assertEquals("", secrets.getDatabasePassword());

        // load secrets
        try {
            secrets.loadServerSecrets();
            Assert.assertNotEquals("", secrets.getDatabaseUser());
            Assert.assertNotEquals("", secrets.getDatabasePassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

}
