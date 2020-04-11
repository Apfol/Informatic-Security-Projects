package com.mycompany.homework3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
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

/**
 * Class
 *
 * @author Andres Ramos
 * @author Carlos Gutierrez
 * @version 1.0
 * @see https://www.alvarodeleon.net/encriptar-y-desencriptar-con-rsa-en-java/
 * @author Alvaro Leon Methods taken from reference: genKeyPair(); encrypt(); without the file
 *     execution decrypt(); without the file execution
 */
public class RSA {


     static final String PUBLIC_KEY_NAME = "public.pem";
  static final String PUBLIC_KEY_NAME_DELIVER = "publicBonifacio.pem";

  static final String PUBLIC_KEY_PATH = "keys/" + PUBLIC_KEY_NAME;
  static final String PUBLIC_KEY_PATH_DELIVER = "keys/" + PUBLIC_KEY_NAME_DELIVER;
  static final String PRIVATE_KEY_NAME = "private.pri";
  static final String PRIVATE_KEY_PATH = "keys/" + PRIVATE_KEY_NAME;
  static final String ENCRYPTED_FILES_FOLDER_NAME = "encrypt";
  static final String FILE_ENCRYPTED_PATH = ENCRYPTED_FILES_FOLDER_NAME + "/file-encrypted";
  static final String DECRYPTED_FILES_FOLDER_NAME = "decrypt";
  static final String FILE_DECRYPTED_PATH = DECRYPTED_FILES_FOLDER_NAME + "/file-decrypted";
  static final String FILE_ENCRYPTED_PATH_Deliver =
      ENCRYPTED_FILES_FOLDER_NAME + "/file-encryptedanastacia";
  static final int MAXIMUN_FILE_ENCRYPT = 117;
  static final int MAXIMUN_FILE_DECRYPT = 128;

  private PrivateKey privateKey = null;
  private PublicKey publicKey = null;
  private File privateKeyFile = new File(PRIVATE_KEY_PATH);
  private File publicKeyFile = new File(PUBLIC_KEY_PATH);
  private File fileToEncrypt;
  private File fileToDecrypt;

    /**
     * Method to get the public key path with the name of the file
     *
     * @return Returns a string with the public path file
     */
    
    public String getPublickeyPathNAME(){
        return PUBLIC_KEY_PATH;
    }
    /**
     * Method to get the public key path with the name of the file with an name alteration to be stored
     *
     * @return Returns a string with the public path file name altered
     */
    public String getPublickeyPathNAMEdeliver(){
        return PUBLIC_KEY_PATH_DELIVER;
    }
    /**
     * Method to get the path where the encrypted file is stored
     *
     * @return Returns a string with the encrypted path file
     */
    public String getFileEncryptedpath(){
        return FILE_ENCRYPTED_PATH;
    }
    /**
     * Method to get the encrypted file path with an name alteration to be stored
     *
     * @return Returns a string with the encrypted path file with an alteration
     */
    public String getFileEncryptedpathdeliver(){
        return FILE_ENCRYPTED_PATH_Deliver;
    }
    /**
     * Method to get the public key as a file
     *
     * @return Returns the pubic key file
     */
    public File getPublicKeyFile() {
        return publicKeyFile;
    }

    /**
     * Method to set the private key as a file
     *
     * @param privateKeyFile A file that contains the private key
     */
    public void setPrivateKeyFile(File privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    /**
     * Method to set the public key as a file
     *
     * @param publicKeyFile A file that contains the public key, with extension
     * PEM
     */
    public void setPublicKeyFile(File publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    /**
     * Method to get the selected file to encrypt
     *
     * @return returns the selected file
     */
    public File getFileToEncrypt() {
        return fileToEncrypt;
    }

    /**
     * Method to set the selected file to encrypt
     *
     * @param fileToEncrypt A file that contains some kind of information and
     * will be encrypted. Extensions pretty like txt or cvs
     */
    public void setFileToEncrypt(File fileToEncrypt) {
        this.fileToEncrypt = fileToEncrypt;
    }

    /**
     * Method to get the selected file to decrypt
     *
     * @return Returns the selected decrypt file
     */
    public File getFileToDecrypt() {
        return fileToDecrypt;
    }

    /**
     * Method to set the selected file to encrypt
     *
     * @param fileToDecrypt A file that contains some kind of information and
     * will be decrypted.
     */
    public void setFileToDecrypt(File fileToDecrypt) {
        this.fileToDecrypt = fileToDecrypt;
    }

    /**
     * Method to transform a string key parameter to a byte array,then creates a
     * private key with the parameters given from the byte array
     *
     * @param key An String parameter with the key
     * @throws NoSuchAlgorithmException Throws an exception when an
     * Cryptographic algorithm is requested but is not available.
     * @throws InvalidKeySpecException Throws an exception when there is an invalid
     * encoding key
     * 
     */
    public void setPrivateKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedPrivateKey = stringToBytes(key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        this.privateKey = keyFactory.generatePrivate(privateKeySpec);
    }

    /**
     * Method to transform a string key parameter to a byte array,then creates a
     * public key with the parameters given from the byte array
     *
     * @param key An String parameter with the key
     * @throws NoSuchAlgorithmException Throws an exception when an
     * Cryptographic algorithm is requested but is not available.
     * @throws InvalidKeySpecException Throws an exception when there is an invalid
     * encoding key
     */
    public void setPublicKeyString(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] encodedPublicKey = stringToBytes(key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        this.publicKey = keyFactory.generatePublic(publicKeySpec);
    }

    /**
     * Method to get a private key returned as a string
     *
     * @return String with the private key encoded
     */
    public String getPrivateKeyString() {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());
        return bytesToString(pkcs8EncodedKeySpec.getEncoded());
    }

    /**
     * Method to get a public key returned as a string
     *
     * @return String with the public key encoded
     */
    public String getPublicKeyString() {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(this.publicKey.getEncoded());
        return bytesToString(x509EncodedKeySpec.getEncoded());
    }

    /**
     * Method to generate a pair of keys (Private and public) then settled on
     * the class constructor
     *
     * @param size An int parameter with the size specification to generate
     * public and private keys
     * @throws NoSuchAlgorithmException Throws an exception when an
     * Cryptographic algorithm is requested but is not available.
     * @throws InvalidKeyException Throws an exception when there is an invalid
     * encoding key
     * @throws NoSuchPaddingException Throws an exception when there is an
     * particular padding mechanism requested but is not available
     * @throws IllegalBlockSizeException Throws an exception when there is an
     * length of data provided to a block cipher is incorrect
     * @throws BadPaddingException Throws an exception when the provided data is
     * not padding properly
     */
    public void genKeyPair(int size) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(size);
        KeyPair kp = kpg.genKeyPair();

        this.privateKey = kp.getPrivate();
        this.publicKey = kp.getPublic();
    }

    /**
     * Method to encrypt the file previously settled. Transform the file into a
     * byte array, then it is encoded into a Base64 standard, finally drops it
     * into the file encrypted path. "encrypt" directory.
     *
     * @throws NoSuchAlgorithmException Throws an exception when an
     * Cryptographic algorithm is requested but is not available.
     * @throws InvalidKeyException Throws an exception when there is an invalid
     * encoding key
     * @throws NoSuchPaddingException Throws an exception when there is an
     * particular padding mechanism requested but is not available
     * @throws IllegalBlockSizeException Throws an exception when there is an
     * length of data provided to a block cipher is incorrect
     * @throws BadPaddingException Throws an exception when the provided data is
     * not padding properly
     * @throws InvalidKeySpecException Throws an exception when there is an
     * different spec of the key provided
     * @throws UnsupportedEncodingException Throws an exception when the file
     * encoding is different form expected
     * @throws NoSuchProviderException Throws an exception when the security
     * provider is requested but is not available
     */
    public void encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchProviderException, IOException {
        byte[] encryptedBytes;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
        byte[] fileToEncryptBytes = Files.readAllBytes(fileToEncrypt.toPath());
        byte[] fileToEncryptBytesEncoded = Base64.getEncoder().encode(fileToEncryptBytes);
        byte[] fileToEncryptBytesDecoded = Base64.getDecoder().decode(fileToEncryptBytesEncoded);
    try (FileOutputStream fos =
        new FileOutputStream(
            FILE_ENCRYPTED_PATH + "." + getFileExtension(fileToEncrypt.getAbsolutePath()).get())) {
      byte[] maximunBytes = new byte[MAXIMUN_FILE_ENCRYPT];
      int counter = 0;
      if (fileToEncryptBytesDecoded.length <= MAXIMUN_FILE_ENCRYPT) {

        encryptedBytes = cipher.doFinal(fileToEncryptBytesDecoded);
        fos.write(encryptedBytes);
      } else {
        for (int i = 0; i < fileToEncryptBytesDecoded.length; i++) {
          maximunBytes[counter] = fileToEncryptBytesDecoded[i];
          if (counter == MAXIMUN_FILE_ENCRYPT - 1) {
            encryptedBytes = cipher.doFinal(maximunBytes);
            fos.write(encryptedBytes);
            maximunBytes = new byte[MAXIMUN_FILE_ENCRYPT];
            counter = 0;
          } else if (i == fileToEncryptBytesDecoded.length - 1) {
            encryptedBytes = cipher.doFinal(maximunBytes);
            fos.write(encryptedBytes);
          } else {
            counter++;
          }
        }
      }
    }
  }

  /**
   * Method to decrypt the file previously settled. Transform the file into a byte array, then drops
   * it into the file decrypted path. "decrypt" directory.
   *
   * @throws NoSuchAlgorithmException Throws an exception when an Cryptographic algorithm is
   *     requested but is not available.
   * @throws InvalidKeyException Throws an exception when there is an invalid encoding key
   * @throws NoSuchPaddingException Throws an exception when there is an particular padding
   *     mechanism requested but is not available
   * @throws IllegalBlockSizeException Throws an exception when there is an length of data provided
   *     to a block cipher is incorrect
   * @throws BadPaddingException Throws an exception when the provided data is not padding properly
   * @throws FileNotFoundException Throws an exception when the requested file is not available.
   * @throws UnsupportedEncodingException Throws an exception when the file encoding is different
   *     form expected
   */
  public void decrypt()
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
          IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException {
    byte[] decryptedBytes;

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
    byte[] fileToDecryptBytes = Files.readAllBytes(fileToDecrypt.toPath());

    try (FileOutputStream fos =
        new FileOutputStream(
            FILE_DECRYPTED_PATH + "." + getFileExtension(fileToDecrypt.getAbsolutePath()).get())) {
      byte[] maximunBytes = new byte[MAXIMUN_FILE_DECRYPT];
      int counter = 0;
      if (fileToDecryptBytes.length <= MAXIMUN_FILE_DECRYPT) {
        decryptedBytes = cipher.doFinal(fileToDecryptBytes);
        fos.write(decryptedBytes);
      } else {
        for (int i = 0; i < fileToDecryptBytes.length; i++) {
          maximunBytes[counter] = fileToDecryptBytes[i];
          if (counter == MAXIMUN_FILE_DECRYPT - 1) {
            decryptedBytes = cipher.doFinal(maximunBytes);
            fos.write(decryptedBytes);
            maximunBytes = new byte[MAXIMUN_FILE_DECRYPT];
            counter = 0;
          } else if (i == fileToDecryptBytes.length - 1) {
            decryptedBytes = cipher.doFinal(maximunBytes);
            fos.write(decryptedBytes);
          } else {
            counter++;
          }
        }
      }
    }
  }

  /**
   * Method that get the file extension of an file and check if it is null
   *
   * @param filename String with the filename that would be used
   * @return Returns the file extension, if it is not null
   */
  public Optional<String> getFileExtension(String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1));
  }

  /**
   * Method that transform a set of bytes to a string
   *
   * @param b A byte array that may contains a set of bytes.
   * @return Returns a string with the byte array encoded into a bigInteger
   */
  public String bytesToString(byte[] b) {
    byte[] b2 = new byte[b.length + 1];
    b2[0] = 1;
    System.arraycopy(b, 0, b2, 1, b.length);
    return new BigInteger(b2).toString(36);
  }

  /**
   * Method that transform a String to a set of bytes.
   *
   * @param s A string that may contains a value to be converted into a byte set.
   * @return Returns a set of bytes(Array)
   */
  public byte[] stringToBytes(String s) {
    byte[] b2 = new BigInteger(s, 36).toByteArray();
    return Arrays.copyOfRange(b2, 1, b2.length);
  }

  /**
   * Method that saves the private key to a specific path settled
   *
   * @param path A String with the path to save the private key file
   * @return Returns Null
   * @throws IOException Throws an exception when the main process does not finish as expected
   */
  public String saveToDiskPrivateKey(String path) throws IOException {
    this.privateKeyFile = new File(path);
    FileOutputStream fileOutput = new FileOutputStream(privateKeyFile);
    try (Writer out = new BufferedWriter(new OutputStreamWriter(fileOutput, "UTF-8"))) {
      out.write(this.getPrivateKeyString());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * Method that saves the public key to a specific path settled
   *
   * @param path A String with the path to save the public key file
   * @return Returns Null
   * @throws Exception Throws an exception when the main process does not finish as expected
   */
  public String saveToDiskPublicKey(String path) throws Exception {
    this.publicKeyFile = new File(path);
    FileOutputStream fileOutput = new FileOutputStream(publicKeyFile);
    try (Writer out = new BufferedWriter(new OutputStreamWriter(fileOutput, "UTF-8"))) {
      out.write(this.getPublicKeyString());
      return publicKeyFile.getAbsolutePath();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * Method that find out the public key path and set it
   *
   * @param path A String with the path where the public key is stored
   * @throws IOException Throws an exception when the main process does not finish as expected
   * @throws NoSuchAlgorithmException Throws an exception when the cryptographic algorithm is
   *     requested but it is not available
   * @throws InvalidKeySpecException Throws an exception when the selected key file does not have
   *     the correct specifications.
   */
  public void openFromDiskPublicKey(String path)
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    String content = this.readFileAsString(path);
    this.setPublicKeyString(content);
  }

  /**
   * Method that find out the private key path and set it
   *
   * @param path A String with the path where the private key is stored
   * @throws IOException Throws an exception when the main process does not finish as expected
   * @throws NoSuchAlgorithmException Throws an exception when the cryptographic algorithm is
   *     requested but it is not available
   * @throws InvalidKeySpecException Throws an exception when the selected key file does not have
   *     the correct specifications.
   */
  public void openFromDiskPrivateKey(String path)
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    String content = this.readFileAsString(path);
    this.setPrivateKeyString(content);
  }

  /**
   * Method that recover a file to be encrypt or decrypt
   *
   * @param filePath A String with the path where a objective file is stored
   * @throws IOException Throws an exception when the main process does not finish as expected
   * @return String with the file data prepared to be encrypt or decrypt
   */
  private String readFileAsString(String filePath) throws IOException {
    StringBuilder fileData = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
