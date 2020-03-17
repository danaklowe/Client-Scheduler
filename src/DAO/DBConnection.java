
package DAO;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Dana K Lowe
 */
public class DBConnection {
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U06F9E";
    
    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;
    
    // Driver & Connection interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    static Connection conn;
    
    private static final String username = "U06F9E"; // Username
    private static final String password = "53688746578"; // Password
    
    public static Connection startConnection() throws ClassNotFoundException, SQLException{
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection was successful!");

        return conn;
    }
    
    public static void closeConnection() throws SQLException{
        conn.close();
        System.out.println("Connection closed.");
    }
}
