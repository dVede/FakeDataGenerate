import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.IntStream;

public class DataGenerate {

    public static void main(String[] args) throws IOException {
        if (args.length < 12) {
            throw new IllegalArgumentException();
        }
        generateDatabase(Boolean.parseBoolean(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]),
                Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]),
                Integer.parseInt(args[8]), Integer.parseInt(args[9]), Integer.parseInt(args[10]), Integer.parseInt(args[11]));
    }

    private static void generateDatabase(boolean isFirst, int publisherQuantity, int authorQuantity, int consumerQuantity,
                                         int bookQuantity, int orderQuantity, int authorBookQuantity, int bookGenresQuantity,
                                         int commentQuantity, int orderItemsQuantity, int ratingQuantity, int wishlistQuantity) throws IOException {
        if (isFirst) GenreGenerator.generate();

        IntStream.range(0, authorQuantity).forEach(i -> AuthorGenerator.generate());
        IntStream.range(0, publisherQuantity).forEach(i -> PublisherGenerator.generate());
        IntStream.range(0, consumerQuantity).forEach(i -> ConsumerGenerator.generate());

        final int[] args = getID();

        IntStream.range(0, orderQuantity).map(i -> args[2]).forEach(OrderGenerator::generate);
        IntStream.range(0, bookQuantity).map(i -> args[3]).forEach(BookGenerator::generate);
        IntStream.range(0, authorBookQuantity).forEach(i -> AuthorBookGenerator.generate(args[0], args[1]));
        IntStream.range(0, bookGenresQuantity).forEach(i -> BookGenresGenerator.generate(args[1], args[5]));
        IntStream.range(0, commentQuantity).forEach(i -> CommentGenerator.generate(args[1], args[2]));
        IntStream.range(0, orderItemsQuantity).forEach(i -> OrderItemsGenerator.generate(args[1], args[4]));
        IntStream.range(0, ratingQuantity).forEach(i -> RatingGenerator.generate(args[1], args[2]));
        IntStream.range(0, wishlistQuantity).forEach(i -> WishlistGenerator.generate(args[1], args[2]));
    }

    public static int[] getID() {
        int[] args = new int[6];
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = Objects.requireNonNull(connection).createStatement();
            args[0] = Utils.getLastId(statement, "id", "author");
            args[1] = Utils.getLastId(statement, "id", "book");
            args[2] = Utils.getLastId(statement, "id", "consumer");
            args[3] = Utils.getLastId(statement, "id", "publisher");
            args[4] = Utils.getLastId(statement, "id", "\"order\"");
            args[5] = Utils.getLastId(statement, "id", "genres");
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
        return args;
    }
}
