package main.java.util;

public class ServerSecrets {

	private String databaseUser;
	private String databasePassword;

	public ServerSecrets(String databaseUser, String databasePassword) {
		this.databaseUser = databaseUser;
		this.databasePassword = databasePassword;
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
}
