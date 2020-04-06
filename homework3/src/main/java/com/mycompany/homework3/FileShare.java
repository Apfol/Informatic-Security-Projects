/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework3;

import java.io.IOException;
import java.security.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author chang
 */
public class FileShare {

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

    static void creaClaves(RSA rsa) throws Exception {
        rsa.genKeyPair(1024);
        //Las guardamos asi podemos usarlas despues
        //a lo largo del tiempo
        rsa.saveToDiskPrivateKey(RSA.PRIVATE_KEY_PATH);
        String publicKeyPath = rsa.saveToDiskPublicKey(RSA.PUBLIC_KEY_PATH);
        JOptionPane.showMessageDialog(null, "Claves generadas en el directorio:\n" + publicKeyPath.substring(0, publicKeyPath.length() - RSA.PUBLIC_KEY_NAME.length()));
    }

    static void cifrarArchivo(RSA rsa) throws Exception {

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
        rsa.setFileToEncrypt(fcFileToEncrypt.getSelectedFile());

        rsa.encrypt();
        JOptionPane.showMessageDialog(null, "Archivo encriptado guardado en el directorio:\n" + rootProjectPath + RSA.ENCRYPTED_FILES_FOLDER_NAME);
    }

    static void descifrarArchivo(RSA rsa) throws Exception {
        String rootProjectPath = rsa.getPublicKeyFile().getAbsolutePath().substring(0, rsa.getPublicKeyFile().getAbsolutePath().length() - RSA.PUBLIC_KEY_PATH.length());
        System.out.println(rootProjectPath);
        // Create a File Chooser for public key
        JFileChooser fcPrivateKey = new JFileChooser(rsa.getPublicKeyFile().getAbsolutePath().substring(0, rsa.getPublicKeyFile().getAbsolutePath().length() - RSA.PUBLIC_KEY_NAME.length()));
        fcPrivateKey.setDialogTitle("Seleccionar llave privada");
        //Execute File Choseer for public key
        fcPrivateKey.showOpenDialog(null);
        rsa.openFromDiskPrivateKey(fcPrivateKey.getSelectedFile().getAbsolutePath());
        rsa.setPrivateKeyFile(fcPrivateKey.getSelectedFile());
        // Create a File Chooser for file to encrypt
        JFileChooser fcFileToDecrypt = new JFileChooser(rootProjectPath + RSA.ENCRYPTED_FILES_FOLDER_NAME);
        fcFileToDecrypt.setDialogTitle("Seleccionar archivo a desencriptar");
        fcFileToDecrypt.showOpenDialog(null);
        rsa.setFileToDecrypt(fcFileToDecrypt.getSelectedFile());

        rsa.decrypt();
        JOptionPane.showMessageDialog(null, "Archivo encriptado guardado en el directorio:\n" + rootProjectPath + RSA.DECRYPTED_FILES_FOLDER_NAME);
    }
}
