import com.github.javafaker.Faker;
import org.postgresql.util.Base64;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Objects;

public class ConsumerGenerator {

    private static final String WHITE_SPACE = " ";
    private static final String ZIP_CODE = "###### ";

    public static void generate() {
        final Faker faker = new Faker(new Locale("en"));
        final Faker faker2 = new Faker(new Locale("ru"));
        final String password = faker.internet().password();
        final byte[] saltB = Utils.saltGenerate();
        final byte[] hashedB = Utils.hash(password.toCharArray(), saltB);

        final String login = faker.name().username();
        final String hashed = Base64.encodeBytes(hashedB);
        final String salt = Base64.encodeBytes(saltB);
        final String email = faker.internet().emailAddress();
        final String address = faker2.address().fullAddress().replace(ZIP_CODE, faker2.address().zipCode() + WHITE_SPACE);
        final String telephone = Utils.phoneNumberGenerate();
        insert(login, hashed, salt, email, address, telephone);
    }

    private static void insert(String login, String hash, String salt, String email, String address, String telephone) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO consumer (login, password_hash, password_salt, email," +
                    " address, telephone) VALUES ('%s','%s','%s','%s','%s','%s')", login, hash, salt, email,
                    address, telephone));

        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
