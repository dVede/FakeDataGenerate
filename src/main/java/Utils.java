import Generators.DatabaseConnection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static Generators.DatabaseConnection.getConnection;

public class Utils {

    public static Integer getLastId(Statement statement, String column, String table) {
        Integer id = null;
        try  {
            ResultSet resultSet = statement.executeQuery( "SELECT " + column + " FROM " + table + " ORDER BY id DESC LIMIT 1");
            resultSet.next();
            id = resultSet.getInt(column);
        } catch (SQLException throwable) {
            System.out.println("Error while connecting to DB");
        }
        return id;
    }

    public static void deleteAllInfo() {
        try (Connection connection = getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute("DELETE FROM author_book;" +
                    "DELETE FROM comment;\n" +
                    "DELETE FROM wishlist;\n" +
                    "DELETE FROM rating;\n" +
                    "DELETE FROM genres_book;\n" +
                    "DELETE FROM orderitems;\n" +
                    "DELETE FROM \"order\";\n" +
                    "DELETE FROM consumer;\n" +
                    "DELETE FROM author;\n" +
                    "DELETE FROM book;\n" +
                    "DELETE FROM publisher;\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteConsumerInfo() {
        try (Connection connection = getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute("DELETE FROM consumer;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static class BarAction implements Runnable {
        public void run() {
            System.out.println("Barrier opened");
        }
    }

    public static int getPublisherLastId() {
        int id = 0;
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()){
            id = Utils.getLastId(statement, "id", "publisher");
        } catch (SQLException throwables) {
            System.out.println("Error while connecting to DB");
            throwables.printStackTrace();
        }
        return id;
    }

    public static int getConsumerLastId() {
        int id = 0;
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()){
            id = Utils.getLastId(statement, "id", "consumer");
        } catch (SQLException throwables) {
            System.out.println("Error while connecting to DB");
            throwables.printStackTrace();
        }
        return id;
    }

    public static void writeInFile(String path, List<String> list) throws IOException {
        final List<String> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            final String time = list.get(i);
            res.add(i, time);
        }
        Files.write(Paths.get(path), res, StandardCharsets.UTF_8);
    }
}
