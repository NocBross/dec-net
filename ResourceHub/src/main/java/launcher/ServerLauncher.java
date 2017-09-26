package main.java.launcher;

import main.java.constants.Port;
import main.java.service.CustomService;
import main.java.services.LoginService;
import main.java.services.RegisterService;
import main.java.util.ServerSecrets;
import main.java.watchdog.ServiceWatchdog;

public class ServerLauncher {

    public static void main(String[] args) throws Exception {
        int loginServicePort = Port.LOGIN_SERVICE;
        int registerServicePort = Port.REGISTER_SERVICE;
        ServerSecrets secrets = new ServerSecrets();

        // load secrets
        secrets.loadServerSecrets();

        CustomService[] services = new CustomService[2];
        services[0] = new LoginService(loginServicePort, secrets);
        services[1] = new RegisterService(registerServicePort, secrets);

        ServiceWatchdog serviceWatchdog = new ServiceWatchdog(services);

        // start services
        for (int i = 0; i < services.length; i++) {
            services[i].start();
        }

        // start watchdog
        serviceWatchdog.start();
    }

}
