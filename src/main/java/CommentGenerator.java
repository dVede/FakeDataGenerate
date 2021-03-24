import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class CommentGenerator {

    public static void generate(int bookQuantity, int consumerQuantity) {
        final Faker faker = new Faker();
        final int bookID = faker.number().numberBetween(1, bookQuantity + 1);
        final int consumerID = faker.number().numberBetween(1, consumerQuantity + 1);
        final String comment = faker.lorem().sentence(50, 50);
        insert(bookID, consumerID, comment);
    }

    private static void insert(int bookID, int consumerID, String comment) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO comment (bookid, consumerid, comment) VALUES (" +
                    "'%d','%d','%s')", bookID, consumerID, comment));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
