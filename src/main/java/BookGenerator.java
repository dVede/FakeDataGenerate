import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Objects;

public class BookGenerator {

    private static final int START_YEAR = 1000;
    private static final int END_YEAR = 2022;
    private static final int MIN_PAGES = 200;
    private static final int MAX_PAGES = 7000;
    private static final int MIN_STOCK = 0;
    private static final int MAX_STOCK = 1000;
    private static final int MIN_PRICE = 300;
    private static final int MAX_PRICE = 2000;
    private static final int WORDS_NUM = 50;

    public static void generate(int publisherQuantity) {
        final Faker faker = new Faker(new Locale("ru"));
        final int publisherID = faker.number().numberBetween(1, publisherQuantity + 1);
        final String ISBN = faker.code().isbn13();
        final String imageURL = faker.avatar().image();
        final String title = faker.book().title().replace("'", "''");
        final String year = String.valueOf(faker.number().numberBetween(START_YEAR, END_YEAR));
        final String pages = String.valueOf(faker.number().numberBetween(MIN_PAGES, MAX_PAGES));
        final int numberInStock = faker.number().numberBetween(MIN_STOCK, MAX_STOCK);
        final double price = faker.number().randomDouble(2, MIN_PRICE, MAX_PRICE);
        final String description = faker.lorem().sentence(WORDS_NUM, WORDS_NUM);
        insert(publisherID, ISBN, imageURL, title, year, pages, numberInStock, price, description);
    }

    private static void insert(int publisherID, String ISBN, String imageURL, String title, String year,
                              String pages, int numberInStock, double price, String description) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format(Locale.US,"INSERT INTO book " +
                            "(publisherid, isbn, imageurl, title, year, pages, numberinstock, price, description) VALUES (" +
                    "'%d','%s','%s','%s','%s','%s','%d','%f','%s')", publisherID, ISBN, imageURL, title, year, pages, numberInStock,
                    price, description));
        } catch (SQLException e) {
            System.out.printf("'%d','%s','%s','%s','%s','%s','%d','%f','%s')", publisherID, ISBN, imageURL, title, year,
                    pages, numberInStock, price, description);
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
