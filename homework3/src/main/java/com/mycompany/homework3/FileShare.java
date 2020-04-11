package com.mycompany.homework3;

import java.io.IOException;

import java.security.*;
import static java.sql.DriverManager.println;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Main class, work as the interface with the RSA class.
 *
 * @author Andres Ramos
 * @author Carlos Gutierrez
 * @version 1.0
 * @see RSA
 */
public class FileShare {

    static RSA rsa = new RSA();
    static Servidor server = null;
    static Cliente client = null;

    /**
     * Main method with the interface interaction
     *
     * @param args String array with the user interaction
     * @throws NoSuchAlgorithmException Throws an exception when the
     * cryptographic algorithm is requested but it is not available
     * @throws IOException Throws an exception when the main process does not
     * finish as expected
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, Exception {

        int option;
        do {
            option = Integer.parseInt(JOptionPane.showInputDialog("Bienvenido\n ¿Qué deseas hacer? \n\n"
                    + "1. Crear claves.\n"
                    + "2. Cifrar archivo.\n"
                    + "3. Descifrar archivo.\n"
                    + "4. Iniciar servidor(Resivir archivo).\n"
                    + "5. Iniciar cliente (enviar archivo).\n"
                    + "0. Salir."));
            switch (option) {
                case 1:
                    creaClaves(rsa);
                    break;
                case 2:
                    cifrarArchivo(rsa);
                    break;
                case 3:
                    descifrarArchivo(rsa);
                    break;
                case 4:
                    iniciarservidor();
                    break;
                case 5:
                    iniciarcliente();
                    break;

            }
        } while (option != 0);

    }

    /**
     * Method that create rsa keys with specific size of bits(1024), then store
     * to a "keys" folder
     *
     * @param rsa an instance of RSA class
     * @throws Exception Throws an exception when the main process does not
     * finish as expected
     *
     */
    static void creaClaves(RSA rsa) throws Exception {
        /**
         * @author Alvaro leon
         * @see
         * https://www.alvarodeleon.net/encriptar-y-desencriptar-con-rsa-en-java/
         * sections taken from the reference: genKeyPair();
         * saveToDiskPrivateKey();
         */
        rsa.genKeyPair(1024);
        //Stored to be used on other class

        rsa.saveToDiskPrivateKey(RSA.PRIVATE_KEY_PATH);
        String publicKeyPath = rsa.saveToDiskPublicKey(RSA.PUBLIC_KEY_PATH);
        JOptionPane.showMessageDialog(null, "Claves generadas en el directorio:\n" + publicKeyPath.substring(0, publicKeyPath.length() - RSA.PUBLIC_KEY_NAME.length()));
    }

    /**
     * Method that request an public key, then a objective file, finally exports
     * to an encrypted file on the folder encrypt
     *
     * @param rsa an instance of RSA class
     * @throws Exception Throws an exception when the main process does not
     * finish as expected
     *
     */
    public static void cifrarArchivo(RSA rsa) throws Exception {

        //check the current working directory, then save it to rootProjectPath
        String rootProjectPath = rsa.getPublicKeyFile().getAbsolutePath().substring(0, rsa.getPublicKeyFile().getAbsolutePath().length() - RSA.PUBLIC_KEY_PATH.length());
        System.out.println(rootProjectPath);
        // Create a File Chooser for public key
        JFileChooser fcPublicKey = new JFileChooser(rsa.getPublicKeyFile().getAbsolutePath().substring(0, rsa.getPublicKeyFile().getAbsolutePath().length() - RSA.PUBLIC_KEY_NAME.length()));
        fcPublicKey.setDialogTitle("Seleccionar llave pública");
        //Execute File Choseer for public key
        fcPublicKey.showOpenDialog(null);
        rsa.openFromDiskPublicKey(fcPublicKey.getSelectedFile().getAbsolutePath());
        rsa.setPublicKeyFile(fcPublicKey.getSelectedFile());
        // Create a File Chooser for file to encrypt
        JFileChooser fcFileToEncrypt = new JFileChooser(rootProjectPath + "files-to-encrypt-examples");
        fcFileToEncrypt.setDialogTitle("Seleccionar archivo a encriptar");
        fcFileToEncrypt.showOpenDialog(null);
        //Set file to encrypt 
        rsa.setFileToEncrypt(fcFileToEncrypt.getSelectedFile());
        //Call to an RSA class method to encrypt the configured file
        rsa.encrypt();
        JOptionPane.showMessageDialog(null, "Archivo encriptado guardado en el directorio:\n" + rootProjectPath + RSA.ENCRYPTED_FILES_FOLDER_NAME);
    }

    /**
     * Method that request a public key file, then a objective file and finally
     * exports an decrypted file on the decrypt folder
     *
     * @param rsa an instance of RSA class
     * @throws Exception Throws an exception when the main process does not
     * finish as expected
     */
    public static void descifrarArchivo(RSA rsa) throws Exception {
        //check the current working directory, then save it to rootProjectPath
        String rootProjectPath = rsa.getPublicKeyFile().getAbsolutePath().substring(0, rsa.getPublicKeyFile().getAbsolutePath().length() - RSA.PUBLIC_KEY_PATH.length());
        System.out.println(rootProjectPath);
        // Create a File Chooser for public key
        JFileChooser fcPrivateKey = new JFileChooser(rsa.getPublicKeyFile().getAbsolutePath().substring(0, rsa.getPublicKeyFile().getAbsolutePath().length() - RSA.PUBLIC_KEY_NAME.length()));
        fcPrivateKey.setDialogTitle("Seleccionar llave privada");
        //Execute File Choseer for public key
        fcPrivateKey.showOpenDialog(null);
        rsa.openFromDiskPrivateKey(fcPrivateKey.getSelectedFile().getAbsolutePath());
        rsa.setPrivateKeyFile(fcPrivateKey.getSelectedFile());
        // Create a File Chooser for file to decrypt
        JFileChooser fcFileToDecrypt = new JFileChooser(rootProjectPath + RSA.ENCRYPTED_FILES_FOLDER_NAME);
        fcFileToDecrypt.setDialogTitle("Seleccionar archivo a desencriptar");
        fcFileToDecrypt.showOpenDialog(null);
        //Set file to decrypt 
        rsa.setFileToDecrypt(fcFileToDecrypt.getSelectedFile());
        //Call to an RSA class method to decrypt the configured file
        rsa.decrypt();
        JOptionPane.showMessageDialog(null, "Archivo encriptado guardado en el directorio:\n" + rootProjectPath + RSA.DECRYPTED_FILES_FOLDER_NAME);
    }

    /**
     * Method that make a call to start the Servidor class
     *
     * @see Servidor
     *
     * @throws IOException Throws an exception when the main process does not
     * finish as expected
     */
    public static void iniciarservidor() throws IOException {
        server = new Servidor(rsa.getPublickeyPathNAME(), rsa.getFileEncryptedpathdeliver());
        println("iniciando servidor");
        println("/");
    }

    /**
     * Method that make a call to start the Cliente class with parameters: the
     * public key path where the file would be stored and the path where the
     * encrypted file is stored
     *
     * @see Cliente
     */
    public static void iniciarcliente() {

        client = new Cliente(rsa.getPublickeyPathNAMEdeliver(), rsa.getFileEncryptedpath());
    }

    /**
     * Method that make a call to a instance of the class FileShare then use the
     * method descrifrarArchivo();
     *
     * @throws Exception Throws an exception when the main process does not
     * finish as expected
     */
    public void descifrarauto() throws Exception {
        descifrarArchivo(rsa);

    }
}
