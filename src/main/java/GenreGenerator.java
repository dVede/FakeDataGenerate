import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class GenreGenerator {

    private static final String SEPARATOR1 = ":";
    private static final String SEPARATOR2 = "/";

    public static void generate() throws IOException {
        AtomicInteger i = new AtomicInteger(1);
        Files.readAllLines(Paths.get("src/data/genre")).forEach(genre ->{
            String[] genres = genre.split(SEPARATOR1);
            String parentGenre = genres[0];
            int parentID = i.get();
            insert(parentGenre, null);
            i.getAndIncrement();
            if (genres.length > 1) {
                String[] genresList = genres[1].split(SEPARATOR2);
                for (String s : genresList) {
                    insert(s, parentID);
                    i.getAndIncrement();
                }
            }
        });
    }

    private static void insert(String genre, Integer parentID) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement()){
            if (parentID == null) {
                statement.execute(String.format("INSERT INTO genres (genre, parentgenreid) VALUES (" +
                        "'%s', NULL)", genre));
            }
            else statement.execute(String.format("INSERT INTO genres (genre, parentgenreid) VALUES (" +
                    "'%s','%d')", genre, parentID));
        } catch (SQLException e) {
            System.out.println("Error while connecting to DB");
            e.printStackTrace();
        }
    }
}
