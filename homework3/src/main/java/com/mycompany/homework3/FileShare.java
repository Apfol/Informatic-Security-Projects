/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework3;

import java.io.IOException;
import java.security.*;
import javax.swing.JOptionPane;

/**
 *
 * @author chang
 */
public class FileShare {
    
    static final String PUBLIC_KEY_NAME = "keys/rsa.pem";
    static final String PRIVATE_KEY_NAME = "keys/rsa.pri";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, Exception {

        RSA rsa = new RSA();

        int option = Integer.parseInt(JOptionPane.showInputDialog("Bienvenido\n ¿Qué deseas hacer? \n\n"
                + "1. Crear claves.\n"
                + "2. Cifrar archivo.\n"
                + "3. Descifrar archivo."));
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
    }

    static void creaClaves(RSA rsa) throws Exception {
        rsa.genKeyPair(1024);
        //Las guardamos asi podemos usarlas despues
        //a lo largo del tiempo
        rsa.saveToDiskPrivateKey(PRIVATE_KEY_NAME);
        String publicKeyPath = rsa.saveToDiskPublicKey(PUBLIC_KEY_NAME);
        JOptionPane.showMessageDialog(null, "Claves generadas en el directorio:\n" + publicKeyPath.substring(0, publicKeyPath.length() - 7));
    }

    static void cifrarArchivo(RSA rsa) throws Exception {

    }

    static void descifrarArchivo(RSA rsa) {

    }
}
