import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    final static String USER = "postgres";
    final static String password = "postgres";
    final static String URL = "jdbc:postgresql://localhost:5432/book_store";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, password);
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
        }
        return null;
    }
}
