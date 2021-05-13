package Model.Transcations;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateTransactionThread extends TransactionThread {

    private final static String SERIALIZABLE_ERROR = "40001";

    public UpdateTransactionThread(CyclicBarrier cb1, CyclicBarrier cb2,
                                   Connection connection, int amount) {
        super(cb1, cb2, connection, amount);
    }

    @Override
    public List<String> transaction(int quantity, Connection connection) {
        final List<String> timeArr = new ArrayList<>();
        final Faker faker = new Faker(new Locale("en"));
        final String login = faker.name().username();
        boolean check = false;
        for (int i = 0; i < quantity; i++) {
            final long start = System.nanoTime();
            do {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("UPDATE consumer SET login = '" + login +  "' WHERE telephone LIKE '%1'");
                    check = false;
                } catch (SQLException throwables) {
                    if (throwables.getSQLState().equals(SERIALIZABLE_ERROR)) check = true;
                    else throwables.printStackTrace();
                }
            } while (check);
            final long end = System.nanoTime();
            timeArr.add(String.format("%d %d", end - super.getThreadStartTime(), end-start));
        }
        return timeArr;
    }
}
