package utils;
// Placeholder for password hashing utilities. In production use bcrypt or similar.
public class PasswordUtils {
    public static String hash(String plain) { return plain; }
    public static boolean verify(String plain, String hash) { return plain.equals(hash); }
}
