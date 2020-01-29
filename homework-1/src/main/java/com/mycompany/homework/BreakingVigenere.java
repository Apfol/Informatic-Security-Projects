/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.swing.JOptionPane;

/**
 *
 * @author apfol
 */
public class BreakingVigenere {

    final static int POSITION_NOT_FOUND = 30;

    public static void main(String[] args) {

        String plain;
        String encrypted;
        String key;
        int keyLength;

        int option = Integer.parseInt(JOptionPane.showInputDialog("¿Cuál método deseas ejecutar? \n"
                + "1. Encrypt. \n"
                + "2. Crack. \n"
                + "3. Verify."));

        switch (option) {
            case 1:
                plain = JOptionPane.showInputDialog("Introduce el texto a cifrar:");
                key = JOptionPane.showInputDialog("Introduce la llave:");
                String textEncripted = encrypt(plain, key);
                System.out.println(textEncripted);
                break;
            case 2:
                encrypted = JOptionPane.showInputDialog("Introduce el texto cifrado:");
                keyLength = Integer.parseInt(JOptionPane.showInputDialog("Introduce la longitud de la llave:"));
                crack(encrypted, keyLength);
                break;
            case +3:
                plain = JOptionPane.showInputDialog("Introduce el texto descifrado:");
                encrypted = JOptionPane.showInputDialog("Introduce el texto cifrado:");
                key = JOptionPane.showInputDialog("Introduce la llave:");
                boolean isEquals = verify(plain, encrypted, key);
                if (isEquals) {
                    JOptionPane.showMessageDialog(null, "Es igual");
                } else {
                    JOptionPane.showMessageDialog(null, "No es igual");
                }
                break;
        }

    }

    static String encrypt(String plain, String key) {
        String textEncripted = "";
        String keyLonger = getKeyLonger(plain, key);
        char[][] vigenereTable = createVigenereTable();
        for (int i = 0; i < plain.length(); i++) {
            int textPosition = getWordPosition(String.valueOf(plain.charAt(i)));
            int keyPosition = getWordPosition(String.valueOf(keyLonger.charAt(i)));
            textEncripted += vigenereTable[keyPosition][textPosition];
        }
        return textEncripted;
    }

    static String[] crack(String text, int keylen) {

        String[] subcryptograms = new String[keylen];
        int counter = 0;

        //Obtener subcriptogramas
        for (int sub = 0; sub < subcryptograms.length; sub++) {
            subcryptograms[sub] = "";
            subcryptograms[sub] += String.valueOf(text.charAt(sub));
            for (int i = sub; i < text.length(); i++) {
                if (counter == keylen) {
                    subcryptograms[sub] += String.valueOf(text.charAt(i));
                    counter = 1;
                } else {
                    counter++;
                }
            }
            counter = 0;
        }

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        ArrayList<HashMap<String, Integer>> subcryptogramsFrecuencies = new ArrayList<>();

        //Obtener frecuencias
        for (int sub = 0; sub < subcryptograms.length; sub++) {
            subcryptogramsFrecuencies.add(new HashMap<>());
            for (int characterPosition = 0; characterPosition < alphabet.length(); characterPosition++) {
                int frecuencyCounter = 0;
                for (int encryptedPosition = 0; encryptedPosition < text.length(); encryptedPosition++) {
                    if (String.valueOf(alphabet.charAt(characterPosition)).toLowerCase().equals(String.valueOf(text.charAt(encryptedPosition)).toLowerCase())) {
                        frecuencyCounter++;
                    }
                }
                subcryptogramsFrecuencies.get(sub).put(String.valueOf(alphabet.charAt(characterPosition)), frecuencyCounter);
            }
        }
        System.out.println(subcryptogramsFrecuencies);
        //Organizar frecuencias de mayor a menor en cada subcriptograma
        return new String[keylen];
    }

    static boolean verify(String plain, String encrypted, String key) {
        String textPlainEncrypted = encrypt(plain, key);
        return textPlainEncrypted.equals(encrypted);
    }

    static char[][] createVigenereTable() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[][] vigenereTable = new char[26][26];
        for (int c = 0; c < alphabet.length(); c++) {
            for (int r = 0; r < alphabet.length(); r++) {
                vigenereTable[c][r] = alphabet.charAt(r);
            }
            alphabet = exchangeAlphabet(alphabet);
        }
        return vigenereTable;
    }

    private static String exchangeAlphabet(String alphabet) {
        String alphabetExchanged = "";
        for (int i = 0; i < alphabet.length() - 1; i++) {
            alphabetExchanged += String.valueOf(alphabet.charAt(i + 1));
        }
        alphabetExchanged += alphabet.charAt(0);
        return alphabetExchanged;
    }

    private static String getKeyLonger(String plain, String key) {
        String keyLonger = "";
        int wordKeyPosition = 0;
        int wordTextPosition = 0;
        while (wordTextPosition < plain.length()) {
            while (wordKeyPosition < key.length()) {
                if (plain.length() > keyLonger.length()) {
                    keyLonger += String.valueOf(key.charAt(wordKeyPosition));
                    wordKeyPosition++;
                    wordTextPosition++;
                } else {
                    break;
                }
            }
            wordKeyPosition = 0;
        }
        return keyLonger;
    }

    private static int getWordPosition(String word) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < alphabet.length(); i++) {
            if (String.valueOf(alphabet.charAt(i)).toLowerCase().equals(word.toLowerCase())) {
                return i;
            }
        }
        return POSITION_NOT_FOUND;
    }
}
