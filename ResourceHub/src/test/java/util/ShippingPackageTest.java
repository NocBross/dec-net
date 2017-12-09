package test.java.util;

import org.junit.Assert;
import org.junit.Test;

import main.java.util.ShippingPackage;

public class ShippingPackageTest {

    @Test
    public void test() {
        try {
            testGET();
            testOther();
            testPOST();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    private void testGET() throws Exception {
        String data = "some data";
        String requestMethod = "GET";
        String resource = "http://localhost/nick/profile";
        ShippingPackage packet = new ShippingPackage(data, requestMethod, resource);

        // test values
        Assert.assertEquals(data, packet.getData());
        Assert.assertEquals(requestMethod, packet.getRequestMethod());
        Assert.assertEquals(resource, packet.getResource());
    }

    private void testOther() {
        try {
            new ShippingPackage("test", "test", "test");
            throw new AssertionError();
        } catch (Exception e) {

        }
    }

    private void testPOST() throws Exception {
        String data = "some data";
        String requestMethod = "POST";
        String resource = "http://localhost/nick/profile";
        ShippingPackage packet = new ShippingPackage(data, requestMethod, resource);

        // test values
        Assert.assertEquals(data, packet.getData());
        Assert.assertEquals(requestMethod, packet.getRequestMethod());
        Assert.assertEquals(resource, packet.getResource());
    }

}
