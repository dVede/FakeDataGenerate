import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Objects;

public class PublisherGenerator {

    private static final String WHITE_SPACE = " ";
    private static final String ZIP_CODE= "###### ";

    public static void generate() {
        final Faker faker = new Faker(new Locale("ru"));
        final Faker faker2 = new Faker(new Locale("en"));
        final String name = faker.book().publisher().replace("'", "''");;
        final String address = faker.address().fullAddress();
        final String resAddress = address.replace(ZIP_CODE, faker.address().zipCode() + WHITE_SPACE);
        final String email = faker2.internet().emailAddress();
        insert(name, resAddress, email);
    }

    private static void insert(String name, String resAddress, String email) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO publisher (name, address, email) VALUES (" +
                    "'%s','%s','%s')", name, resAddress, email));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
