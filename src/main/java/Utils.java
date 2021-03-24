import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;

public class Utils {

    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String[] NUM_CODE = {"+7", "7", "8"};
    private static final long START_DATE = Timestamp.valueOf("2000-01-01 00:00:00").getTime();
    private static final long END_DATE = Timestamp.valueOf("2021-01-01 00:00:00").getTime();

    public static String phoneNumberGenerate() {
        return String.format("%s9%d", NUM_CODE[randNum(0, 3)], randNum(100000000, 1000000000 ));
    }

    public static int randNum(int a, int b) {
        return (int) (Math.random() * (b - a)) + a;
    }

    public static byte[] saltGenerate() {
        byte[] salt = new byte[32];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static Timestamp getTimestamp(){
        final long diff = END_DATE - START_DATE + 1;
        return new Timestamp(START_DATE + (long)(Math.random() * diff));
    }

    public static Integer getLastId(Statement statement, String column, String table) {
        Integer id = null;
        try  {
            ResultSet resultSet = statement.executeQuery( "SELECT " + column + " FROM " + table + " ORDER BY id DESC LIMIT 1");
            resultSet.next();
            id = resultSet.getInt(column);
        } catch (SQLException throwable) {
            System.out.println("Error while connecting to DB");
        }
        return id;
    }
}
