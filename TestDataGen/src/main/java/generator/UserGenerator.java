package main.java.generator;

import main.java.filewriter.UsersFileWriter;
import main.java.util.DatabaseConnector;

public class UserGenerator {

    private String resourceHubAddress;
    private DatabaseConnector database;
    private PasswordGenerator passwordGenerator;
    private UsersFileWriter writer;

    public UserGenerator(String databaseUser, String databasePassword) throws Exception {
        resourceHubAddress = "@localhost";
        database = new DatabaseConnector(databaseUser, databasePassword);
        passwordGenerator = new PasswordGenerator();
        writer = new UsersFileWriter();
    }

    public void deleteTestusers(int startIndex, int endIndex) {
        String nickname = "test_user_";

        writer.getFile().delete();
        for (int i = startIndex; i <= endIndex; i++) {
            database.deleteUser(nickname + i);
        }
    }

    public void generate(int value) throws Exception {
        for (int i = 0; i < value; i++) {
            String nickname = "test_user_" + (i + 1) + resourceHubAddress;
            String password = passwordGenerator.generate();

            database.insertNewUser(nickname, password);
            writer.writeNewUser(nickname, password);
        }
    }

}
