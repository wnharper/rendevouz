package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <h2>Data Base Connection</h2>
 * The DBConnection class establishes and provides a connection to the
 * MYSQL database
 *
 * @author  Warren Harper
 * @version 1.0
 * @since   2021-04-20
 */


public class DBConnection {

    // Database attributes
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ07qP6";
    private static final String jdbcUrl = protocol + vendorName + ipAddress + dbName;
    private static final String mSqlJbcDriver = "com.mysql.jdbc.Driver";
    private static final String username = "U07qP6";
    private static final String password = "53689104902";
    private static Connection dbConnection = null;

    /**
     * Method establishes a connection to the database
     * @return database connection
     */
    public static Connection startConnection() {
        try {
            Class.forName(mSqlJbcDriver);
            dbConnection = DriverManager.getConnection(jdbcUrl, username, password);

            System.out.println("Database Connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }

    /**
     * Method closes connection to the database
     * Try/catch used in case connection already closed
     */
    public static void closeConnection() {
        try {
            dbConnection.close();
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * Method returns the current database connection
     * @return database connection
     */
    public static Connection getDbConnection() {
        return dbConnection;
    }
}
