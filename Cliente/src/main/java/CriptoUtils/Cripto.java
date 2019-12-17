package CriptoUtils;

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

}
