package main.java.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import main.java.constants.Port;
import main.java.constants.SecretKeys;
import main.java.service.CustomService;
import main.java.services.LoginService;
import main.java.services.RegisterService;
import main.java.util.ServerSecrets;
import main.java.watchdog.ServiceWatchdog;

public class ServerLauncher {

    public static void main(String[] args) throws Exception {
        int loginServicePort = Port.LOGIN_SERVICE;
        int registerServicePort = Port.REGISTER_SERVICE;

        // load secrets
        ServerSecrets secrets = loadServerSecrets();

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

    /**
     * Loads the server secrets e.g. pepper and private key for identification.
     * 
     * @return server secrets
     */
    private static ServerSecrets loadServerSecrets() {
        String path = "secrets";
        String line = "";
        String[] splittedLine = null;
        String databaseUser = "";
        String databasePassword = "";
        ServerSecrets secrets = null;

        try {
            BufferedReader databaseSecretsReader = new BufferedReader(new FileReader(new File(path + "/database")));

            while ((line = databaseSecretsReader.readLine()) != null) {
                if (line.length() > 0 && line.charAt(0) == SecretKeys.COMMENT_KEY) {
                    continue;
                }

                splittedLine = line.trim().split(SecretKeys.KEY_VALUE_SEPARATOR);
                switch (splittedLine[0]) {
                    case SecretKeys.DATABASE_USER:
                        databaseUser = splittedLine[1];
                        break;
                    case SecretKeys.DATABASE_PASSWORD:
                        databasePassword = splittedLine[1];
                        break;
                    default:
                        break;
                }
            }

            secrets = new ServerSecrets(databaseUser, databasePassword);
            databaseSecretsReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return secrets;
    }

}
