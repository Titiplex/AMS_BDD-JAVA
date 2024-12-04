package database;

import java.sql.*;
import java.util.Properties;

// Ma connection java à Postgresql est issu du travail de M.Morchid

/**
 * Class that allows you to create a connection to postgresql database.
 */
public class ConnectDatabase {

    private static final String URL = "jdbc:postgresql://pedago.univ-avignon.fr:5432/etd";
    private static final String USER = "uapv2401251";
    private static final String PASSWORD = "hWlAr7";

    private static Connection conn;

    public static Connection getConnection() {

        Properties props = new Properties();
        props.setProperty("user", ConnectDatabase.USER);
        props.setProperty("password", ConnectDatabase.PASSWORD);
        //props.setProperty("ssl", "true");

        try {
            ConnectDatabase.conn = DriverManager.getConnection(URL, props);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ConnectDatabase.conn;
    }

    /**Closes the connection with postgresql database.
     *
     */
    public static void closeConnection() {
        try {
            if (ConnectDatabase.conn != null && !ConnectDatabase.conn.isClosed()) {
                ConnectDatabase.conn.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
