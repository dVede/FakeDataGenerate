package Generators;

import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Objects;

public class GenreGenerator {
    public static void generate() throws IOException {
        MutableInt i = new MutableInt(1);
        for (String genre : Files.readAllLines(Paths.get("src/data/genre"))) {
            String[] genres = genre.split(GeneratorUtils.SEPARATOR1);
            String parentGenre = genres[0];
            int parentID = i.getAndIncrement();
            insert(parentGenre, null);
            if (genres.length > 1) {
                for (int j = 1; j < genres.length; j++) {
                    insert(genres[j], parentID);
                    i.incrementAndGet();
                }
            }
        }
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
