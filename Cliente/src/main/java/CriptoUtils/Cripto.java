package CriptoUtils;


import com.google.gson.JsonObject;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.getInstance;



public class Cripto {
    private static final String ENC_ALG = "RSA";

    public static byte[] sign( PrivateKey privateKey, byte[] data ) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign( privateKey );
        signature.update( data );

        return signature.sign();
    }

    public static boolean verifySignature( PublicKey publicKey, byte[] signedData, byte[] originalData ) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify( publicKey );
        signature.update( originalData );
        return signature.verify( signedData );
    }

    public static byte[] decrypt(byte[] data, PrivateKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher c = getInstance(ENC_ALG);
        c.init(DECRYPT_MODE, key);
        return c.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, PublicKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance(ENC_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static PublicKey getKey(byte[] byteKey){
        try{
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance(ENC_ALG);

            return kf.generatePublic(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static PrivateKey getPrivateKey(byte[] byteKey){
        try{
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance(ENC_ALG);

            return kf.generatePrivate(X509publicKey);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
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

    public static KeyPair generateKeyPair(String alias,KeyStore ks,char[] passphrase) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, InvalidKeyException, NoSuchProviderException, SignatureException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        X509Certificate certificate = generateCertificate(kp);
        java.security.cert.X509Certificate[] certChain = new java.security.cert.X509Certificate[1];
        certChain[0] = certificate;
        ks.setKeyEntry(alias, (Key)kp.getPrivate(), passphrase, certChain);
        return kp;
    }

    public static X509Certificate generateCertificate(KeyPair keyPair) throws NoSuchAlgorithmException, CertificateEncodingException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, 1);
        X509V3CertificateGenerator cert = new X509V3CertificateGenerator();
        cert.setSerialNumber(BigInteger.valueOf(1));   //or generate a random number
        cert.setSubjectDN(new X509Principal("CN=localhost"));  //see examples to add O,OU etc
        cert.setIssuerDN(new X509Principal("CN=localhost")); //same since it is self-signed
        cert.setPublicKey(keyPair.getPublic());
        cert.setNotBefore(new Date());
        cert.setNotAfter(c.getTime());
        cert.setSignatureAlgorithm("SHA256withRSA");
        PrivateKey signingKey = keyPair.getPrivate();
        return cert.generate(signingKey);
    }

    public static PrivateKey getPrivate(String alias,KeyStore ks,char[] passphrase) throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        return (PrivateKey) ks.getKey(alias, passphrase);
    }

    public static PublicKey getPublic(String alias,KeyStore ks,char[] passphrase) throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        Certificate cert = ks.getCertificate(alias);
        return cert.getPublicKey();
    }



}
