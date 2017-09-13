package main.java.constants;

public class Database {

	/**
	 * java database connector driver
	 */
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	/**
	 * Name of the server database.
	 */
	public static final String NAME = "RessourceHub_data";

	/**
	 * URL of the database which is used to execute queries.
	 */
	public static final String DB_URL = "jdbc:mysql://localhost/" + NAME
			+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&"
			+ "serverTimezone=UTC&useSSL=false";

	/**
	 * URL of the database server.
	 */
	public static final String DB_URL_INIT = "jdbc:mysql://localhost?useUnicode=true&useJDBCCompliantTimezoneShift=true&"
			+ "useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
}
