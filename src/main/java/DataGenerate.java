import Generators.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.IntStream;

public class DataGenerate {

    public static void main(String[] args) throws IOException {
        TransactionCreate transactionCreate = new TransactionCreate();
        transactionCreate.createThreads(100, 4);
    }

    private static void generateDatabase(boolean isFirst, int publisherQuantity, int authorQuantity, int consumerQuantity,
                                         int bookQuantity, int orderQuantity, int authorBookQuantity, int bookGenresQuantity,
                                         int commentQuantity, int orderItemsQuantity, int ratingQuantity, int wishlistQuantity) throws IOException {
        if (isFirst) GenreGenerator.generate();

        IntStream.range(0, authorQuantity).forEach(i -> AuthorGenerator.generate());
        IntStream.range(0, publisherQuantity).forEach(i -> PublisherGenerator.generate());
        IntStream.range(0, consumerQuantity).forEach(i -> ConsumerGenerator.generate());

        IntStream.range(0, bookQuantity).map(i -> Utils.getPublisherLastId()).forEach(BookGenerator::generate);
        IntStream.range(0, orderQuantity).map(i -> Utils.getConsumerLastId()).forEach(OrderGenerator::generate);

        final int[] args = getID();

        AuthorBookGenerator.generate(args[0], args[1]);
        BookGenresGenerator.generate(args[1]);
        OrderItemsGenerator.generate(args[1], args[3]);
        IntStream.range(0, ratingQuantity).forEach(i -> RatingGenerator.generate(args[1], args[2]));
        IntStream.range(0, wishlistQuantity).forEach(i -> WishlistGenerator.generate(args[1], args[2]));
        IntStream.range(0, commentQuantity).forEach(i -> CommentGenerator.generate(args[1], args[2]));

    }

    private static int[] getID() {
        int[] args = new int[5];
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()) {
            args[0] = Utils.getLastId(statement, "id", "author");
            args[1] = Utils.getLastId(statement, "id", "book");
            args[2] = Utils.getLastId(statement, "id", "consumer");
            args[3] = Utils.getLastId(statement, "id", "\"order\"");
            args[4] = Utils.getLastId(statement, "id", "genres");
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
        return args;
    }
}
