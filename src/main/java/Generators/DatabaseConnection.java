package Generators;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String URL = "jdbc:postgresql://localhost:5432/book_store";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
        }
        return null;
    }

    public static Connection getConnection(int isolationLvl) {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.setTransactionIsolation(isolationLvl);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
