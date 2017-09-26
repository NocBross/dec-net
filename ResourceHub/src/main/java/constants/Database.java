package main.java.constants;

public class Database {

    /**
     * java database connector driver
     */
    public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Name of the server database.
     */
    public static final String NAME = "ResourceHub_data";

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

    /**
     * Relative path to the file which contains the database secrets like user and
     * password.
     */
    public static final String DATABASE_SECRET_PATH = "secrets/database";
}
