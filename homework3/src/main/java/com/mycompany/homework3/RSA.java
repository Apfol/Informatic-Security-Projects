package com.mycompany.homework3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

    static final String PUBLIC_KEY_NAME = "public.pem";
    static final String PUBLIC_KEY_PATH = "keys/" + PUBLIC_KEY_NAME;
    static final String PRIVATE_KEY_NAME = "private.pri";
    static final String PRIVATE_KEY_PATH = "keys/" + PRIVATE_KEY_NAME;
    static final String FILE_ENCRYPTED_PATH = "files/file-encrypted";
    static final String FILE_DECRYPTED_PATH = "files/file-decrypted";

    private PrivateKey privateKey = null;
    private PublicKey publicKey = null;
    private File privateKeyFile = new File(PRIVATE_KEY_PATH);
    private File publicKeyFile = new File(PUBLIC_KEY_PATH);
    private File fileToEncrypt;
    private File fileToDecrypt;

    public File getPrivateKeyFile() {
        return privateKeyFile;
    }

    public File getPublicKeyFile() {
        return publicKeyFile;
    }

    public void setPrivateKeyFile(File privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public void setPublicKeyFile(File publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public File getFileToEncrypt() {
        return fileToEncrypt;
    }

    public void setFileToEncrypt(File fileToEncrypt) {
        this.fileToEncrypt = fileToEncrypt;
    }

    public File getFileToDecrypt() {
        return fileToDecrypt;
    }

    public void setFileToDecrypt(File fileToDecrypt) {
        this.fileToDecrypt = fileToDecrypt;
    }

    public void setPrivateKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedPrivateKey = stringToBytes(key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        this.privateKey = keyFactory.generatePrivate(privateKeySpec);
    }

    public void setPublicKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] encodedPublicKey = stringToBytes(key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        this.publicKey = keyFactory.generatePublic(publicKeySpec);
    }

    public String getPrivateKeyString() {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());
        return bytesToString(pkcs8EncodedKeySpec.getEncoded());
    }

    public String getPublicKeyString() {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(this.publicKey.getEncoded());
        return bytesToString(x509EncodedKeySpec.getEncoded());
    }

    public void genKeyPair(int size) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(size);
        KeyPair kp = kpg.genKeyPair();

        this.privateKey = kp.getPrivate();
        this.publicKey = kp.getPublic();
    }

    public void encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchProviderException, IOException {
        byte[] encryptedBytes;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
        byte[] fileToEncryptBytes = Files.readAllBytes(fileToEncrypt.toPath());
        byte[] fileToEncryptBytesEncoded = Base64.getEncoder().encode(fileToEncryptBytes);
        byte[] fileToEncryptBytesDecoded = Base64.getDecoder().decode(fileToEncryptBytesEncoded);
        encryptedBytes = cipher.doFinal(fileToEncryptBytesDecoded);

        try ( FileOutputStream fos = new FileOutputStream(FILE_ENCRYPTED_PATH + "." + getFileExtension(fileToEncrypt.getAbsolutePath()).get())) {
            fos.write(encryptedBytes);
        }
    }

    public void decrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException {
        byte[] decryptedBytes;
        

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        byte[] fileToDecryptBytes = Files.readAllBytes(fileToDecrypt.toPath());
        decryptedBytes = cipher.doFinal(fileToDecryptBytes);

        try ( FileOutputStream fos = new FileOutputStream(FILE_DECRYPTED_PATH + "." + getFileExtension(fileToDecrypt.getAbsolutePath()).get())) {
            fos.write(decryptedBytes);
        }
    }

    public Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

    public byte[] stringToBytes(String s) {
        byte[] b2 = new BigInteger(s, 36).toByteArray();
        return Arrays.copyOfRange(b2, 1, b2.length);
    }

    public String saveToDiskPrivateKey(String path) throws IOException {
        this.privateKeyFile = new File(path);
        FileOutputStream fileOutput = new FileOutputStream(privateKeyFile);
        try ( Writer out = new BufferedWriter(new OutputStreamWriter(fileOutput, "UTF-8"))) {
            out.write(this.getPrivateKeyString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String saveToDiskPublicKey(String path) throws Exception {
        this.publicKeyFile = new File(path);
        FileOutputStream fileOutput = new FileOutputStream(publicKeyFile);
        try ( Writer out = new BufferedWriter(new OutputStreamWriter(fileOutput, "UTF-8"))) {
            out.write(this.getPublicKeyString());
            return publicKeyFile.getAbsolutePath();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void openFromDiskPublicKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String content = this.readFileAsString(path);
        this.setPublicKeyString(content);
    }

    public void openFromDiskPrivateKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String content = this.readFileAsString(path);
        this.setPrivateKeyString(content);
    }

    private String readFileAsString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder();
        try ( BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
        }
        return fileData.toString();
    }

}
