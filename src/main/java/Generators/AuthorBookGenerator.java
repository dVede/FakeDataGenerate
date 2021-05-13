package Generators;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Objects;

public class AuthorBookGenerator {
    public static void generate(int authorQuantity, int bookQuantity) {
        for (int i = 0; i < bookQuantity + 1; i++) {
            final Faker faker = new Faker(new Locale("en"));
            final int authorID = faker.number().numberBetween(1, authorQuantity + 1);
            insert(authorID, i);
        }

    }

    private static void insert(int authorID, int bookID) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO author_book (authorid, bookid) VALUES ('%d','%d')" +
                            " ON CONFLICT (authorid, bookid) DO NOTHING", authorID, bookID));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
