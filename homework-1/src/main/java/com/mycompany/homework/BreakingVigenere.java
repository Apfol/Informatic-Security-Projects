/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework;

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
                // Método Harcore
                break;
            case 3:
                plain = JOptionPane.showInputDialog("Introduce el texto a descifrado:");
                encrypted = JOptionPane.showInputDialog("Introduce el texto a cifrado:");
                key = JOptionPane.showInputDialog("Introduce la llave:");
                boolean isEquals = verify(plain, encrypted, key);
                if(isEquals) {
                    JOptionPane.showInputDialog(null, "Es igual");
                } else {
                    JOptionPane.showInputDialog(null, "No es igual");
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

    String[] crack(String text, int keylen) {
        return new String[keylen];
    }

    static boolean verify(String plain, String encrypted, String key) {
        boolean isEquals = false;
        String textDesencrypted = "";
        String keyLonger = getKeyLonger(plain, key);
        char[][] vigenereTable = createVigenereTable();
        for (int i = 0; i < encrypted.length(); i++) {
            int encryptedPosition = getWordPosition(String.valueOf(encrypted.charAt(i)));
            int keyPosition = getWordPosition(String.valueOf(keyLonger.charAt(i)));
            textDesencrypted += vigenereTable[keyPosition][encryptedPosition];
        }
        if (plain.toLowerCase().equals(textDesencrypted.toLowerCase())) {
            isEquals = true;
        }
        return isEquals;
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
