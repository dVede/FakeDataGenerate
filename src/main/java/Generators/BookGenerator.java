package Generators;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Objects;

public class BookGenerator {

    public static void generate(int publisherQuantity) {
        final Faker faker = new Faker(new Locale("ru"));
        final int publisherID = faker.number().numberBetween(1, publisherQuantity + 1);
        final String ISBN = faker.code().isbn13();
        final String imageURL = faker.avatar().image();
        final String title = faker.book().title().replace(GeneratorUtils.APOSTROPHE, GeneratorUtils.DOUBLE_APOSTROPHE);
        final String year = String.valueOf(faker.number().numberBetween(GeneratorUtils.START_YEAR, GeneratorUtils.END_YEAR));
        final String pages = String.valueOf(faker.number().numberBetween(GeneratorUtils.MIN_PAGES, GeneratorUtils.MAX_PAGES));
        final int numberInStock = faker.number().numberBetween(GeneratorUtils.MIN_STOCK, GeneratorUtils.MAX_STOCK);
        final double price = faker.number().randomDouble(2, GeneratorUtils.MIN_PRICE, GeneratorUtils.MAX_PRICE);
        final String description = faker.lorem().sentence(GeneratorUtils.WORDS_NUM, GeneratorUtils.WORDS_NUM);
        insert(publisherID, ISBN, imageURL, title, year, pages, numberInStock, price, description);
    }

    private static void insert(int publisherID, String ISBN, String imageURL, String title, String year,
                              String pages, int numberInStock, double price, String description) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format(Locale.US,"INSERT INTO book " +
                            "(publisherid, isbn, imageurl, title, year, pages, numberinstock, price, description)" +
                            "VALUES ('%d','%s','%s','%s','%s','%s','%d','%f','%s')", publisherID, ISBN, imageURL,
                    title, year, pages, numberInStock, price, description));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
