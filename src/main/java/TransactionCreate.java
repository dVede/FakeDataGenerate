import Generators.DatabaseConnection;
import Model.Transcations.InsertTransactionThread;
import Model.Transcations.SelectTransactionThread;
import Model.Transcations.UpdateTransactionThread;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TransactionCreate {

    public void createThreads(int amount, int isolationLvl) throws IOException {
        final CyclicBarrier cb1 = new CyclicBarrier(3, new Utils.BarAction());
        final CyclicBarrier cb2 = new CyclicBarrier(4, new Utils.BarAction());

        final Connection connectionSelect = DatabaseConnection.getConnection(isolationLvl);
        final Connection connectionInsert = DatabaseConnection.getConnection(isolationLvl);
        final Connection connectionUpdate = DatabaseConnection.getConnection(isolationLvl);

        final SelectTransactionThread selectThread = new SelectTransactionThread(cb1, cb2, connectionSelect, amount);
        final InsertTransactionThread insertThread = new InsertTransactionThread(cb1, cb2, connectionInsert, amount);
        final UpdateTransactionThread updateThread = new UpdateTransactionThread(cb1, cb2, connectionUpdate, amount);
        insertThread.start();
        selectThread.start();
        updateThread.start();

        try {
            cb2.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        try {
            connectionInsert.close();
            connectionSelect.close();
            connectionUpdate.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        final String outPathSelect = String.format("src/transOutput/select_%d_%d_4",amount, isolationLvl);
        final String outPathInsert = String.format("src/transOutput/insert_%d_%d_4",amount, isolationLvl);
        final String outPathUpdate = String.format("src/transOutput/update_%d_%d_4",amount, isolationLvl);

        Utils.writeInFile(outPathSelect, selectThread.getResult());
        Utils.writeInFile(outPathInsert, insertThread.getResult());
        Utils.writeInFile(outPathUpdate, updateThread.getResult());
    }
}


