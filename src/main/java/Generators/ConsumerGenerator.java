package Generators;

import Model.Consumer;
import com.github.javafaker.Faker;
import org.postgresql.util.Base64;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ConsumerGenerator {

    public static void generate() {
        String[] consumer = generateConsumer();
        insert(consumer[0], consumer[1], consumer[2], consumer[3], consumer[4], consumer[5]);
    }

    private static String[] generateConsumer(){
        String[] res = new String[6];

        final Faker faker = new Faker(new Locale("en"));
        final Faker faker2 = new Faker(new Locale("ru"));
        final String password = faker.internet().password();
        final byte[] saltB = GeneratorUtils.saltGenerate();
        final byte[] hashedB = GeneratorUtils.hash(password.toCharArray(), saltB);

        res[0] = faker.name().username();
        res[1] = Base64.encodeBytes(hashedB);
        res[2] = Base64.encodeBytes(saltB);
        res[3] = faker.internet().emailAddress();
        res[4] = faker2.address().fullAddress().replace(GeneratorUtils.ZIP_CODE, faker2.address().zipCode());
        res[5] = GeneratorUtils.phoneNumberGenerate();

        return res;
    }

    public static List<Consumer> returnGeneratedConsumers(int amount) {
        final List<Consumer> consumers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            String[] res = generateConsumer();
            consumers.add(new Consumer(res[0], res[1], res[2], res[3], res[4], res[5]));
        }
        return consumers;
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
