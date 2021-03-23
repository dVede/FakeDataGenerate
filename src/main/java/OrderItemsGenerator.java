import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class OrderItemsGenerator {

    private static final int AMOUNT_MAX = 1000;
    private static final int AMOUNT_MIN = 1;

    public static void generate(int bookQuantity, int orderQuantity, int orderItemsQuantity) {
        final Faker faker = new Faker();
        for (int i = 0; i < orderItemsQuantity; i++) {
            int amount = faker.number().numberBetween(AMOUNT_MIN, AMOUNT_MAX);
            int bookID = faker.number().numberBetween(1, bookQuantity + 1);
            int orderID = faker.number().numberBetween(1, orderQuantity + 1);
            insert(bookID, orderID, amount);
        }
    }

    private static void insert(int bookID, int orderID, int amount) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO orderitems (bookid, orderid, amount) VALUES (" +
                    "'%d','%d','%d') ON CONFLICT (bookid, orderid) DO NOTHING", bookID, orderID, amount));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
