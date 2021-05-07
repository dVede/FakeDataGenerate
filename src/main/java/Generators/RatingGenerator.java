package Generators;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class RatingGenerator {

    public static void generate(int bookQuantity, int consumerQuantity) {
        final Faker faker = new Faker();
        final int bookID = faker.number().numberBetween(1, bookQuantity + 1);
        final int consumerID = faker.number().numberBetween(1, consumerQuantity + 1);
        final int rating = faker.number().numberBetween(GeneratorUtils.RATING_MIN, GeneratorUtils.RATING_MAX);
        insert(bookID, consumerID, rating);
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
