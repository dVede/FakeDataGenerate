package Model.Transcations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class SelectTransactionThread extends TransactionThread {

    private final static String SERIALIZABLE_ERROR = "40001";

    public SelectTransactionThread(CyclicBarrier cb1, CyclicBarrier cb2, Connection connection, int amount) {
        super(cb1, cb2, connection, amount);
    }

    @Override
    public List<String> transaction(int quantity, Connection connection) {
        final List<String> timeArr = new ArrayList<>();
        AtomicBoolean flag = new AtomicBoolean(false);
        for (int i = 0; i < quantity; i++) {
            final long start = System.nanoTime();
            do {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("SELECT * FROM consumer WHERE telephone LIKE '%1'");
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
