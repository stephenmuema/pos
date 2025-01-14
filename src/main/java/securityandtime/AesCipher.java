package securityandtime;

import com.sun.istack.internal.Nullable;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static securityandtime.config.throwables;

/**
 * AesCipher
 * <p>Encode/Decode text by password using AES-128-CBC algorithm</p>
 */
public class AesCipher {
    private static final int INIT_VECTOR_LENGTH = 16;
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    /**
     * Encoded/Decoded data
     */
    protected String data;
    /**
     * Initialization vector value
     */
    private String initVector;
    /**
     * Error message if operation failed
     */
    private String errorMessage;

    public AesCipher() {
        super();
    }

    /**
     * AesCipher constructor.
     *
     * @param initVector   Initialization vector value
     * @param data         Encoded/Decoded data
     * @param errorMessage Error message if operation failed
     */
    private AesCipher(@Nullable String initVector, @Nullable String data, @Nullable String errorMessage) {
        super();

        this.initVector = initVector;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static int getInitVectorLength() {
        return INIT_VECTOR_LENGTH;
    }

    public static char[] getHexArray() {
        return hexArray;
    }

    /**
     * Encrypt input text by AES-128-CBC algorithm
     *
     * @param secretKey 16/24/32 -characters secret password
     * @param plainText Text for encryption
     * @return Encoded string or NULL if error
     */
    public static AesCipher encrypt(String secretKey, String plainText) {
        String initVector = null;
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }

            // Get random initialization vector
            SecureRandom secureRandom = new SecureRandom();
            byte[] initVectorBytes = new byte[INIT_VECTOR_LENGTH / 2];
            secureRandom.nextBytes(initVectorBytes);
            initVector = bytesToHex(initVectorBytes);
            initVectorBytes = initVector.getBytes(StandardCharsets.UTF_8);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVectorBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Encrypt input text
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(initVectorBytes.length + encrypted.length);
            byteBuffer.put(initVectorBytes);
            byteBuffer.put(encrypted);

            // Result is base64-encoded string: initVector + encrypted result
            String result = Base64.encodeBase64String(byteBuffer.array());

            // Return successful encoded object
            return new AesCipher(initVector, result, null);
        } catch (Throwable t) {
            t.printStackTrace();
            // Operation failed
            return new AesCipher(initVector, null, t.getMessage());
        }
    }

    /**
     * Decrypt encoded text by AES-128-CBC algorithm
     *
     * @param secretKey  16/24/32 -characters secret password
     * @param cipherText Encrypted text
     * @return Self object instance with data or error message
     */
    public static AesCipher decrypt(String secretKey, String cipherText) {
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }

            // Get raw encoded data
            byte[] encrypted = Base64.decodeBase64(cipherText);

            // Slice initialization vector
            IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted, 0, INIT_VECTOR_LENGTH);
            // Set secret password
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Trying to get decrypted text
            String result = new String(cipher.doFinal(encrypted, INIT_VECTOR_LENGTH, encrypted.length - INIT_VECTOR_LENGTH));

            // Return successful decoded object
            return new AesCipher(bytesToHex(ivParameterSpec.getIV()), result, null);
        } catch (Throwable t) {
            t.printStackTrace();
            throwables.put(t.getMessage(), t);
            // Operation failed
            return new AesCipher(null, null, t.getMessage());
        }
    }

    /**
     * Check that secret password length is valid
     *
     * @param key 16/24/32 -characters secret password
     * @return TRUE if valid, FALSE otherwise
     */
    private static boolean isKeyLengthValid(String key) {
        return key.length() == 16 || key.length() == 24 || key.length() == 32;
    }

    /**
     * Convert Bytes to HEX
     *
     * @param bytes Bytes array
     * @return String with bytes values
     */
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Get encoded/decoded data
     */
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * Get initialization vector value
     */
    public String getInitVector() {
        return initVector;
    }

    public void setInitVector(String initVector) {
        this.initVector = initVector;
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Check that operation failed
     *
     * @return TRUE if failed, FALSE otherwise
     */
    public boolean hasError() {
        return this.errorMessage != null;
    }

    /**
     * To string return resulting data
     *
     * @return Encoded/decoded data
     */
    public String toString() {
        return getData();
    }
//public static void main(String[] args){
//    String secretKey = "0123456789abcdef0123456789abcdef";
//    String text = "Some text";
//    System.out.println(secretKey.length());
//    AesCipher encrypted = AesCipher.encrypt(secretKey, text);
//    System.out.println(encrypted);
//    AesCipher decrypted = AesCipher.decrypt(secretKey, encrypted.getData());
//    System.out.println(decrypted);
//}
}

// USAGE
