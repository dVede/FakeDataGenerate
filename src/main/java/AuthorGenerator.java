import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Objects;

public class AuthorGenerator {
    private static final String WHITE_SPACE= " ";
    public static void generate() {
        final Faker faker = new Faker(new Locale("ru"));
        final String[] name = faker.name().nameWithMiddle().split(WHITE_SPACE);
        final String imageURL = faker.avatar().image();
        insert(name[0], name[1], name[2], imageURL);
    }

    private static void insert(String firstName, String lastName, String middleName, String imageURL) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            statement.execute(String.format("INSERT INTO author (lastname, firstname, middlename, imageurl) VALUES (" +
                    "'%s','%s','%s','%s')", lastName, firstName, middleName, imageURL));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}

