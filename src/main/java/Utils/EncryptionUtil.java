/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author Salvatore
 */
import static Utils.Utility.config;
import static Utils.Utility.logfile;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Level;

public class EncryptionUtil {

    private static final String ALGORITHM = config.getString("ALGORITHM");
    private static final List<String> KEYS = Arrays.asList(config.getString("ENCODED_KEYS").split("\\s*,\\s*"));
    private static final String VALIDATION_STRING = config.getString("VALIDATION_STRING");

    // Generate a new AES key
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // AES-128
        return keyGen.generateKey();
    }

    // Encrypt the input data
    public static String encrypt(String input) {
        try {
            SecretKey key = getKeyFromString(KEYS.get(0));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal((input + VALIDATION_STRING).getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            logfile.log(Level.SEVERE, EncryptionUtil.class.getName(), "Encryption failed " + e.getMessage());
            return null;
        }
    }

    public static List<String> search_Encrypt(String input) {
        List<String> encryptedValues = new ArrayList<>();
        for (String keyStr : KEYS) {
            try {
                SecretKey key = getKeyFromString(keyStr);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] encryptedBytes = cipher.doFinal((input + VALIDATION_STRING).getBytes());
                encryptedValues.add(Base64.getEncoder().encodeToString(encryptedBytes));
            } catch (Exception e) {
                logfile.log(Level.SEVERE, EncryptionUtil.class.getName(), "Encryption failed " + e.getMessage());
            }
        }
        return encryptedValues;
    }

    // Metodo per cifrare i byte del file e restituire i byte cifrati
    public static byte[] encryptBase64(byte[] inputBytes) {
        try {
            SecretKey key = getKeyFromString(KEYS.get(0));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(inputBytes);
        } catch (Exception e) {
            logfile.log(Level.SEVERE, EncryptionUtil.class.getName(), "Encryption failed " + e.getMessage());
            return null;
        }
    }

    // Decrypt the input data
    public static String decrypt(String encryptedInput) {
        if (encryptedInput == null) {
            return null;
        }
        for (String keyStr : KEYS) {
            try {
                SecretKey key = getKeyFromString(keyStr);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedInput));
                String decryptedString = new String(decryptedBytes);
                if (decryptedString.endsWith(VALIDATION_STRING)) {
                    return decryptedString.substring(0, decryptedString.length() - VALIDATION_STRING.length());
                }
            } catch (Exception e) {
                if (!e.getMessage().contains("Given final block not properly padded. Such issues can arise if a bad key is used during decryption")) {
                    logfile.log(Level.SEVERE, EncryptionUtil.class.getName(), "Decryption failed " + e.getMessage());
                }
            }
        }

        return "DATO NON DECRIPTABILE";
    }

    // Decrypt the Base64 encoded data and return the decrypted byte array
    public static byte[] decryptBase64(byte[] encryptedBytes) {
        for (String keyStr : KEYS) {
            try {
                SecretKey key = getKeyFromString(keyStr);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);
                return cipher.doFinal(Base64.getDecoder().decode(encryptedBytes));
            } catch (Exception e) {
                logfile.log(Level.SEVERE, EncryptionUtil.class.getName(), "Decryption failed with this key, trying another..." + e.getMessage());
            }
        }
        logfile.log(Level.SEVERE, EncryptionUtil.class.getName(), "Decryption failed for all keys.");
        return new byte[0];
    }

    // Convert a string key to SecretKey
    public static SecretKey getKeyFromString(String keyStr) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr.trim());
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    // Convert a SecretKey to a string
    public static String getStringFromKey(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
