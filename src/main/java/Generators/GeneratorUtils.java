package Generators;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;

public class GeneratorUtils {

    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String[] NUM_CODE = {"+7", "7", "8"};
    private static final long START_DATE = Timestamp.valueOf("2000-01-01 00:00:00").getTime();
    private static final long END_DATE = Timestamp.valueOf("2021-01-01 00:00:00").getTime();

    protected static final String[] STATUS_LIST = {"WAITING_FOR_PAYMENT", "IN_PROGRESS", "COMPLETED", "CANCELLED"};
    protected static final String DOUBLE_APOSTROPHE = "''";
    protected static final String APOSTROPHE = "'";
    protected static final String ZIP_CODE = "######";
    protected static final String WHITE_SPACE = " ";
    protected static final String SEPARATOR1 = "/";
    protected static final int RATING_MIN = 1;
    protected static final int RATING_MAX= 11;
    protected static final int AMOUNT_MAX = 1000;
    protected static final int AMOUNT_MIN = 1;
    protected static final int MIN_SUM = 1000;
    protected static final int MAX_SUM = 10000;
    protected static final int START_YEAR = 1000;
    protected static final int END_YEAR = 2022;
    protected static final int MIN_PAGES = 200;
    protected static final int MAX_PAGES = 7000;
    protected static final int MIN_STOCK = 0;
    protected static final int MAX_STOCK = 1000;
    protected static final int MIN_PRICE = 300;
    protected static final int MAX_PRICE = 2000;
    protected static final int WORDS_NUM = 50;

    protected static String phoneNumberGenerate() {
        return String.format("%s9%d%d", NUM_CODE[randNum(0, 3)], randNum(10000000, 100000000 ), 1);
    }

    protected static int randNum(int a, int b) {
        return (int) (Math.random() * (b - a)) + a;
    }

    protected static byte[] saltGenerate() {
        byte[] salt = new byte[32];
        RANDOM.nextBytes(salt);
        return salt;
    }

    protected static byte[] hash(char[] password, byte[] salt) {
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

    protected static Timestamp getTimestamp(){
        final long diff = END_DATE - START_DATE + 1;
        return new Timestamp(START_DATE + (long)(Math.random() * diff));
    }
}
