import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class BookGenresGenerator {
    public static void generate(int bookQuantity, int genreQuantity, int bookGenresQuantity) {
        final Faker faker = new Faker();
        for (int i = 0; i < bookGenresQuantity; i++) {
            int genreID = faker.number().numberBetween(1, genreQuantity + 1);
            int bookID = faker.number().numberBetween(1, bookQuantity + 1);
            insert(bookID, genreID);
        }
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