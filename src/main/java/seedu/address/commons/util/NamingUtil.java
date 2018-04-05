package seedu.address.commons.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class supports the naming of files to be stored.
 * toHex() code referenced from:
 * https://stackoverflow.com/questions/36162622/how-do-i-generate-a-hash-code-with-hash-sha256-in-java
 */
public class NamingUtil {

    /**
     * This method uses SHA-256 to hash the 2 input strings and returns it.
     */
    public static String generateUniqueName(String details) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashValue = digest.digest(details.getBytes(StandardCharsets.UTF_8));
            return toHex(hashValue);
        } catch (NoSuchAlgorithmException nae) {
            return details;
        }
    }

    /**
     * Turns a byte array into a string hex code
     * @param hashValue is a byte array
     * @return A hex encode of a byte array
     */
    private static String toHex(byte[] hashValue) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashValue) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
