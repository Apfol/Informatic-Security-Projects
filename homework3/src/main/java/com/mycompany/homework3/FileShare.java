
package com.mycompany.homework3;

import java.io.IOException;
import java.security.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Esta clase es la clase main, contiene la interaccion con la clase RSA. Siendo Interfaz.
 * @author Andres Ramos
 * @author Carlos Gutierrez
 * @version 1.0
 * @see RSA
 */
public class FileShare {
/**
        */
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, Exception {

        RSA rsa = new RSA();
        
        int option;
        do {
            option = Integer.parseInt(JOptionPane.showInputDialog("Bienvenido\n ¿Qué deseas hacer? \n\n"
                    + "1. Crear claves.\n"
                    + "2. Cifrar archivo.\n"
                    + "3. Descifrar archivo.\n"
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
            }
        } while (option != 0);

    }
/**
 * Metodo que crea claves rsa de tamaño 1024 bits y almacenada en el directorio "Keys"
 * @param rsa Una instancia de la clase RSA
 * 
        */
    static void creaClaves(RSA rsa) throws Exception {
        /**
         * @author Alvaro leon
         * @see https://www.alvarodeleon.net/encriptar-y-desencriptar-con-rsa-en-java/
         * secciones tomadas de la referencia: genKeyPair(); saveToDiskPrivateKey(); 
        */
        rsa.genKeyPair(1024);
        //Las guardamos asi podemos usarlas despues
        //a lo largo del tiempo
        rsa.saveToDiskPrivateKey(RSA.PRIVATE_KEY_PATH);
        String publicKeyPath = rsa.saveToDiskPublicKey(RSA.PUBLIC_KEY_PATH);
        JOptionPane.showMessageDialog(null, "Claves generadas en el directorio:\n" + publicKeyPath.substring(0, publicKeyPath.length() - RSA.PUBLIC_KEY_NAME.length()));
    }
/**
 * Metodo que solicita una llave publica, luego un archivo objetivo y finalmente exporta un archivo cifrado al directorio encrypt
 * @param rsa Una instancia de RSA
 * 
        */
    
    static void cifrarArchivo(RSA rsa) throws Exception {
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
 * Metodo que solicita una llave publica, luego un archivo objetivo y finalmente exporta un archivo descifrado  al directorio decrypt
 * @param rsa Una instancia de RSA
 * 
        */

    static void descifrarArchivo(RSA rsa) throws Exception {
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
}
