package CriptoUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;

public class Cripto {
    private static final String SIGNATURE_ALGO = "SHA256withRSA";
    private static final String ENC_ALG = "AES/CBC/PKCS5Padding";

    public static IvParameterSpec genIVforAES() throws NoSuchPaddingException, NoSuchAlgorithmException {
        SecureRandom randomSecureRandom = new SecureRandom();
        byte[] iv = new byte[Cipher.getInstance("AES").getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static byte[] decrypt(byte[] data, SecretKeySpec key, IvParameterSpec IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c = Cipher.getInstance(ENC_ALG);
        c.init(Cipher.DECRYPT_MODE, key, IV);
        return c.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, SecretKeySpec key, IvParameterSpec IV) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher c = Cipher.getInstance(ENC_ALG);
        c.init(Cipher.ENCRYPT_MODE, key, IV);
        return c.doFinal(data);
    }

    public static String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
