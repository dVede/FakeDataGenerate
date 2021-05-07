package Generators;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class BookGenresGenerator {
    public static void generate(int bookQuantity, int genreQuantity) {
        final Faker faker = new Faker();
        final int genreID = faker.number().numberBetween(1, genreQuantity + 1);
        final int bookID = faker.number().numberBetween(1, bookQuantity + 1);
        insert(bookID, genreID);
    }

    private static void insert(int bookID, int genreID) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO genres_book (genresid, bookid) VALUES (" +
                    "'%d','%d') ON CONFLICT (genresid, bookid) DO NOTHING", genreID, bookID));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
