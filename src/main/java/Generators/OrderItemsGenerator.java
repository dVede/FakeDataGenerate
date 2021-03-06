package Generators;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class OrderItemsGenerator {

    public static void generate(int bookQuantity, int orderQuantity) {
        final Faker faker = new Faker();
        for (int i = 1; i < orderQuantity + 1; i++) {
            final int amount = faker.number().numberBetween(GeneratorUtils.AMOUNT_MIN, GeneratorUtils.AMOUNT_MAX);
            final int bookID = faker.number().numberBetween(1, bookQuantity + 1);
            insert(bookID, i, amount);
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
