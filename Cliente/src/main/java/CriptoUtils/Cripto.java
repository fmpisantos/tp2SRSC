package CriptoUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static javax.crypto.Cipher.*;

public class Cripto {
    private static final String SIGNATURE_ALGO = "SHA256withRSA";
    private static final String ENC_ALG = "AES/CBC/PKCS5Padding";

    public static IvParameterSpec genIVforAES() throws NoSuchPaddingException, NoSuchAlgorithmException {
        SecureRandom randomSecureRandom = new SecureRandom();
        byte[] iv = new byte[getInstance("AES").getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static byte[] decrypt(byte[] data, SecretKeySpec key, IvParameterSpec IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c = getInstance(ENC_ALG);
        c.init(DECRYPT_MODE, key, IV);
        return c.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, SecretKeySpec key, IvParameterSpec IV) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher c = getInstance(ENC_ALG);
        c.init(ENCRYPT_MODE, key, IV);
        return c.doFinal(data);
    }

}
