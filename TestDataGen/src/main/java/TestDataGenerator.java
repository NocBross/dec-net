package main.java;

import java.io.File;

import main.java.constants.OutputFiles;
import main.java.constants.Secrets;
import main.java.generator.UserGenerator;
import main.java.util.ServerSecrets;

public class TestDataGenerator {

	public static void main(String[] args) throws Exception {
		File data = new File(OutputFiles.PATH_PREFIX);
		ServerSecrets secrets = new ServerSecrets(Secrets.DATABASE_SECRET_PATH);
		secrets.loadServerSecrets();
		UserGenerator userGenerator = new UserGenerator(secrets.getDatabaseUser(), secrets.getDatabasePassword());

		if (args[0].equals("delete")) {
			userGenerator.deleteTestusers(Integer.valueOf(args[1]), Integer.valueOf(args[2]));

			if (data.listFiles().length == 0) {
				data.delete();
			}
		} else {
			userGenerator.generate(Integer.valueOf(args[0]));
		}
	}

}
