import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class RatingGenerator {

    public static final int RATING_MIN = 1;
    public static final int RATING_MAX= 11;

    public static void generate(int bookQuantity, int consumerQuantity, int ratingQuantity) {
        final Faker faker = new Faker();
        for (int i = 0; i < ratingQuantity; i++) {
            int bookID = faker.number().numberBetween(1, bookQuantity + 1);
            int consumerID = faker.number().numberBetween(1, consumerQuantity + 1);
            int rating = faker.number().numberBetween(RATING_MIN, RATING_MAX);
            insert(bookID, consumerID, rating);
        }
    }

    private static void insert(int bookID, int consumerID, int rating) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO rating (bookid, consumerid, rating) VALUES (" +
                    "'%d','%d','%d') ON CONFLICT (bookid, consumerid) DO NOTHING", bookID, consumerID, rating));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
