package main.java.launcher;

import main.java.constants.Network;
import main.java.service.CustomService;
import main.java.services.LoginService;
import main.java.services.ShippingService;
import main.java.services.WebService;
import main.java.util.ServerSecrets;
import main.java.watchdog.ServiceWatchdog;

public class ServerLauncher {

    public static void main(String[] args) throws Exception {
        ServerSecrets secrets = new ServerSecrets();

        // load secrets
        secrets.loadServerSecrets();

        CustomService[] services = new CustomService[2];
        services[0] = new ShippingService();
        services[1] = new LoginService(Network.NETWORK_HUB_PORT, secrets, (ShippingService) services[0]);
        WebService webService = new WebService(Network.SERVER_WEBSERVICE_PORT, secrets);

        ServiceWatchdog serviceWatchdog = new ServiceWatchdog(services, webService);

        // start services
        for (int i = 0; i < services.length; i++) {
            services[i].start();
        }
        webService.startService();

        // start watchdog
        serviceWatchdog.start();
    }

}
