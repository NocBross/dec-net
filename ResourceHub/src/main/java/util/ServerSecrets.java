package main.java.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import main.java.constants.Database;
import main.java.constants.SecretKeys;

public class ServerSecrets {

    private String databaseUser;
    private String databasePassword;
    private File databaseSecrets;

    public ServerSecrets() {
        this.databaseUser = "";
        this.databasePassword = "";
        databaseSecrets = new File(Database.DATABASE_SECRET_PATH);
    }

    public ServerSecrets(String path) {
        this.databaseUser = "";
        this.databasePassword = "";
        databaseSecrets = new File(path);
    }

    /**
     * Returns the database user
     * 
     * @return database user
     */
    public String getDatabaseUser() {
        return databaseUser;
    }

    /**
     * Returns the database password.
     * 
     * @return database password
     */
    public String getDatabasePassword() {
        return databasePassword;
    }

    /**
     * Loads the server secrets e.g. database user and password for identification.
     */
    public void loadServerSecrets() throws Exception {
        String line = "";
        String[] splittedLine = null;

        BufferedReader databaseSecretsReader = new BufferedReader(new FileReader(databaseSecrets));

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

        databaseSecretsReader.close();
    }
}
