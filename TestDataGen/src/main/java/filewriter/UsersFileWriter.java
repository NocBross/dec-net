package main.java.filewriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import main.java.constants.CSV;
import main.java.constants.OutputFiles;

public class UsersFileWriter {

    private File users;
    private BufferedWriter writer;

    public UsersFileWriter() throws Exception {
        users = new File(OutputFiles.USERS);

        if (!users.getParentFile().exists()) {
            users.getParentFile().mkdir();
        }

        if (!users.exists()) {
            users.createNewFile();
        }

        writer = new BufferedWriter(new FileWriter(users));
        writer.write(CSV.USERS_HEAD + System.lineSeparator());
        writer.flush();
    }

    /**
     * Closes the UsersFileWriter.
     * 
     * @throws Exception
     *             - If something went wrong.
     */
    public void close() throws Exception {
        writer.close();
    }

    /**
     * Returns the file which is used by this writer.
     * 
     * @return file
     */
    public File getFile() {
        return users;
    }

    /**
     * Writes the given nickname and password of a new test user into the users
     * file.
     * 
     * @param nickname
     *            - nickname of the new user
     * @param password
     *            - password of the new user
     * @throws Exception
     *             - If something went wrong.
     */
    public void writeNewUser(String nickname, String password) throws Exception {
        writer.write(nickname + CSV.SEPARATOR + password + System.lineSeparator());
        writer.flush();
    }

}
