package test.java.util;

import org.junit.Assert;
import org.junit.Test;

import main.java.util.ServerSecrets;

public class ServerSecretsTest {

    @Test
    public void test() {
        testWithoutPath();
        testWithPath();
    }

    private void testWithoutPath() {
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

    private void testWithPath() {
        String path = "secrets/testSecrets.txt";
        ServerSecrets secrets2 = new ServerSecrets(path);

        // test initial values
        Assert.assertEquals("", secrets2.getDatabaseUser());
        Assert.assertEquals("", secrets2.getDatabasePassword());

        // load secrets
        try {
            secrets2.loadServerSecrets();
            throw new AssertionError();
        } catch (Exception e) {

        }
    }

}
