package Model.Transcations;

import Generators.ConsumerGenerator;
import Model.Consumer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class InsertTransactionThread extends TransactionThread {
    private final List<Consumer> consumers;
    private final static String SERIALIZABLE_ERROR = "40001";

    public InsertTransactionThread(CyclicBarrier cb1, CyclicBarrier cb2, Connection connection, int amount) {
        super(cb1, cb2, connection, amount);
        consumers = ConsumerGenerator.returnGeneratedConsumers(amount);
    }

    @Override
    public List<String> transaction(int quantity, Connection connection) {
        final List<String> timeArr = new ArrayList<>();
        final String sql = "INSERT INTO consumer (login, password_hash, password_salt, email," +
                " address, telephone) VALUES ('%s','%s','%s','%s','%s','%s')";
        AtomicBoolean flag = new AtomicBoolean(false);
        for (Consumer consumer : consumers) {
            final long start = System.nanoTime();
            do {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(String.format(sql, consumer.getLogin(), consumer.getHashed(),
                            consumer.getSalt(), consumer.getEmail(), consumer.getAddress(), consumer.getTelephone()));
                    flag.set(false);
                } catch (SQLException throwables) {
                    if (throwables.getSQLState().equals(SERIALIZABLE_ERROR)) flag.set(true);
                    else throwables.printStackTrace();
                }
            } while (flag.get());
            final long end = System.nanoTime();
            timeArr.add(String.format("%d %d", end - super.getThreadStartTime(), end-start));
        }
        return timeArr;
    }
}
