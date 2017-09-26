package test.java.launcher;

import org.junit.Assert;
import org.junit.Test;

import main.java.launcher.ServerLauncher;

public class LauncherTest {

    @Test
    public void test() {
        Thread server = new Thread() {
            public void run() {
                try {
                    ServerLauncher.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AssertionError();
                }
            }
        };
        server.start();
        Assert.assertTrue(server.isAlive());
        server.interrupt();
    }

}
