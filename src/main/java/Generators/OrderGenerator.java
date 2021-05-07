package Generators;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Objects;

public class OrderGenerator {

    public static void generate(int consumerQuantity) {
        final Faker faker = new Faker(new Locale("en"));
        final int randNumber = faker.number().numberBetween(0, GeneratorUtils.STATUS_LIST.length);
        final int consumerID = faker.number().numberBetween(1, consumerQuantity + 1);
        final Timestamp timestamp = GeneratorUtils.getTimestamp();
        final String status = GeneratorUtils.STATUS_LIST[randNumber];
        final double totalSum = faker.number().randomDouble(2, GeneratorUtils.MIN_SUM, GeneratorUtils.MAX_SUM);
        insert(consumerID, timestamp, status, totalSum);
    }

    private static void insert(int consumerID, Timestamp timestamp, String status, double totalSum) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format(Locale.US,"INSERT INTO \"order\" (consumerid, timestamp, status," +
                    " totalsum) VALUES ('%d','%s','%s','%f')", consumerID, timestamp, status, totalSum));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
