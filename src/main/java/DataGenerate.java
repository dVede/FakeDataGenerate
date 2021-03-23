import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.IntStream;

public class DataGenerate {

    public static void main(String[] args) throws IOException {
        if (args.length < 11) {
            throw new IllegalArgumentException();
        }
        generateDatabase(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]),
                Integer.parseInt(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]), Integer.parseInt(args[10]));
    }

    private static void generateDatabase(int publisherQuantity, int authorQuantity, int consumerQuantity,
                                         int bookQuantity, int orderQuantity, int authorBookQuantity, int bookGenresQuantity,
                                         int commentQuantity, int orderItemsQuantity, int ratingQuantity, int wishlistQuantity) throws IOException {
        IntStream.range(0, authorQuantity).forEach(i -> AuthorGenerator.generate());
        IntStream.range(0, publisherQuantity).forEach(i -> PublisherGenerator.generate());
        IntStream.range(0, consumerQuantity).forEach(i -> ConsumerGenerator.generate());
        GenreGenerator.generate();

        Connection connection = DatabaseConnection.getConnection();
        try {
            Statement statement = Objects.requireNonNull(connection).createStatement();
            Integer authorLastID = Utils.getLastId(statement, "id", "author");
            Integer bookLastID = Utils.getLastId(statement, "id", "book");
            Integer consumerLastID = Utils.getLastId(statement, "id", "consumer");
            Integer publisherLastId = Utils.getLastId(statement, "id", "publisher");
            Integer orderLastID = Utils.getLastId(statement, "id", "\"order\"");
            Integer genresLastId = Utils.getLastId(statement, "id", "genres");

            IntStream.range(0, orderQuantity).map(i -> consumerLastID).forEach(OrderGenerator::generate);
            IntStream.range(0, bookQuantity).map(i -> publisherLastId).forEach(BookGenerator::generate);
            AuthorBookGenerator.generate(authorLastID, bookLastID, authorBookQuantity);
            BookGenresGenerator.generate(bookLastID, genresLastId, bookGenresQuantity);
            CommentGenerator.generate(bookLastID, consumerLastID, commentQuantity);
            OrderItemsGenerator.generate(bookLastID, orderLastID, orderItemsQuantity);
            RatingGenerator.generate(bookLastID, consumerLastID, ratingQuantity);
            WishlistGenerator.generate(bookLastID, consumerLastID, wishlistQuantity);
        } catch (SQLException throwables) {
            System.out.println("Error while connecting to DB");
            throwables.printStackTrace();
        }
    }
}
