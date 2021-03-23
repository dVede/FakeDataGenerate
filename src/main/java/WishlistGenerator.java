import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class WishlistGenerator {

    public static void generate(int bookQuantity, int consumerQuantity, int wishlistQuantity) {
        final Faker faker = new Faker();
        for (int i = 0; i < wishlistQuantity; i++) {
            int bookID = faker.number().numberBetween(1, bookQuantity + 1);
            int consumerID = faker.number().numberBetween(1, consumerQuantity + 1);
            insert(bookID, consumerID);
        }
    }

    private static void insert(int bookID, int consumerID) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO wishlist (consumerid, bookid) VALUES (" +
                    "'%d','%d') ON CONFLICT (consumerid, bookid) DO NOTHING", bookID, consumerID));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
