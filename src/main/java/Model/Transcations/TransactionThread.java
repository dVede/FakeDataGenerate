package Model.Transcations;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public abstract class TransactionThread extends Thread{
    private final CyclicBarrier cb1;
    private final CyclicBarrier cb2;
    private final Connection connection;
    private final int amount;
    private List<String> result;
    private long threadStartTime;

    public TransactionThread(CyclicBarrier cb1, CyclicBarrier cb2, Connection connection, int amount) {
        this.cb1 = cb1;
        this.cb2 = cb2;
        this.connection = connection;
        this.amount = amount;
        this.threadStartTime = 0;
    }

    public abstract List<String> transaction(int quantity, Connection connection);

    public void run() {
        try {
            cb1.await();
            threadStartTime = System.nanoTime();
            result = transaction(amount, connection);
            cb2.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public List<String> getResult() {
        return result;
    }

    public long getThreadStartTime() {
        return threadStartTime;
    }
}
