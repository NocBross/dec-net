package test.java.watchdog;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import main.java.constants.LogFiles;
import main.java.service.CustomService;
import main.java.services.LoginService;
import main.java.services.ShippingService;
import main.java.services.WebService;
import main.java.util.ServerSecrets;
import main.java.watchdog.ServiceWatchdog;

public class ServiceWatchdogTest {

    // attributes
    // end of attributes

    // constructor
    // end of constructor

    // methods
    @Test
    public void test() {
        try {
            ServerSecrets secrets = new ServerSecrets();
            secrets.loadServerSecrets();
            int numberOfServices = 2;
            ShippingService shippingService = new ShippingService();
            WebService webservice = new WebService(19999, secrets, shippingService);
            CustomService[] services = new CustomService[numberOfServices];
            services[0] = shippingService;
            services[1] = new LoginService(20000, secrets, shippingService);
            ServiceWatchdog serviceWatchdog = new ServiceWatchdog(services, webservice);

            // start services and watchdog
            for (int i = 0; i < numberOfServices; i++) {
                services[i].start();
            }
            serviceWatchdog.start();

            // test
            Thread.sleep(31000);
            Assert.assertTrue(services[0].isAlive());
            Assert.assertTrue(services[1].isAlive());
            Assert.assertTrue(serviceWatchdog.isAlive());

            serviceWatchdog.stopWatchdog();
            Thread.sleep(31000);
            Assert.assertTrue(services[0].isAlive());
            Assert.assertTrue(services[1].isAlive());
            Assert.assertFalse(serviceWatchdog.isAlive());
            serviceWatchdog = new ServiceWatchdog(services, webservice);
            serviceWatchdog.start();
            services[0].stopService();
            Thread.sleep(60000);
            Assert.assertFalse(services[0].isAlive());
            Assert.assertFalse(services[1].isAlive());
            Assert.assertFalse(serviceWatchdog.isAlive());

            // clean up
            File file = new File(LogFiles.LOGIN_LOG);
            file.delete();
            file = new File(LogFiles.REGISTER_LOG);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    } // end of test
      // end of methods

} // end of class
