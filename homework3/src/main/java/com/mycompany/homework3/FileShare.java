/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework3;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.*;
import javax.swing.JOptionPane;

/**
 *
 * @author chang
 */
public class FileShare {
    
    static final String PUBLIC_KEY_NAME = "rsa.pem";
    static final String PRIVATE_KEY_NAME = "rsa.pri";

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
        rsa.saveToDiskPublicKey(PUBLIC_KEY_NAME);
    }

    static void cifrarArchivo(RSA rsa) throws Exception {

    }

    static void saveToDiskPrivateKey(String path) throws Exception {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            //out.write(this.getPrivateKeyString());
            out.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    static void saveToDiskPublicKey(String path) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            //out.write(this.getPublicKeyString());
            out.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    static void descifrarArchivo(RSA rsa) {

    }
}
