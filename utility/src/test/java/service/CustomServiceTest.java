package test.java.service;

import java.io.File;
import java.net.ServerSocket;

import org.junit.Assert;
import org.junit.Test;

import main.java.service.CustomService;

public class CustomServiceTest {

    private class TestService extends CustomService {

        private int serviceCallCounter;

        public TestService(int port, String pathToLogFile) throws Exception {
            super(port, pathToLogFile);
            serviceCallCounter = 0;
        }

        public int getServiceCallCounter() {
            return serviceCallCounter;
        }

        @Override
        protected void service() {
            serviceCallCounter++;
        }

    }

    @Test
    public void test() {
        try {
            String logFile = "logs/testlog.txt";
            testWithPort(logFile);
            testWithoutPort(logFile);
            new File(logFile).delete();
            new File(logFile).getParentFile().delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    private void testWithPort(String logFilePath) throws Exception {
        int port = 20000;
        ServerSocket socket = null;
        TestService service = new TestService(port, logFilePath);

        try {
            socket = new ServerSocket(port);
            socket.close();
            throw new AssertionError();
        } catch (Exception e) {
            Assert.assertNull(socket);
        }
        service.start();
        Thread.sleep(100);

        Assert.assertTrue(service.isAlive());
        service.stopService();
        service.join();
        Assert.assertTrue(service.getServiceCallCounter() > 0);
    }

    private void testWithoutPort(String logFilePath) throws Exception {
        int port = -1;
        ServerSocket socket = null;
        TestService service = new TestService(port, logFilePath);

        try {
            socket = new ServerSocket(port);
            socket.close();
            throw new AssertionError();
        } catch (Exception e) {
            Assert.assertNull(socket);
        }
        service.start();
        Thread.sleep(100);

        Assert.assertTrue(service.isAlive());
        service.stopService();
        service.join();
        Assert.assertTrue(service.getServiceCallCounter() > 0);
    }

}
