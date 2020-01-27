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

        String text = JOptionPane.showInputDialog("Introduce el texto a cifrar:");
        String key = JOptionPane.showInputDialog("Introduce la llave:");

        String textEncripted = encrypt(text, key);
        
        System.out.println(textEncripted);
        
    }

    static String encrypt(String text, String key) {
        
        String textEncripted = "";
        String keyLonger = "";

        int wordKeyPosition = 0;
        int wordTextPosition = 0;
        while (wordTextPosition < text.length()) {
            while (wordKeyPosition < key.length()) {
                if (text.length() > keyLonger.length()) {
                    keyLonger += String.valueOf(key.charAt(wordKeyPosition));
                    wordKeyPosition++;
                    wordTextPosition++;
                } else {
                    break;
                }

            }
            wordKeyPosition = 0;
        }
        
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        char[][] vigenereTable = createVigenereTable(alphabet);

        for (int i = 0; i < text.length(); i++) {
            int textPosition = getWordPosition(String.valueOf(text.charAt(i)), alphabet);
            int keyPosition = getWordPosition(String.valueOf(keyLonger.charAt(i)), alphabet);
            textEncripted += vigenereTable[keyPosition][textPosition];
        }

        return textEncripted;
    }

    private static String exchangeAlphabet(String alphabet) {

        String alphabetExchanged = "";

        for (int i = 0; i < alphabet.length() - 1; i++) {
            alphabetExchanged += String.valueOf(alphabet.charAt(i + 1));
        }

        alphabetExchanged += alphabet.charAt(0);

        return alphabetExchanged;
    }

    private static int getWordPosition(String word, String alphabet) {
        for (int i = 0; i < alphabet.length(); i++) {
            if(String.valueOf(alphabet.charAt(i)).toLowerCase().equals(word.toLowerCase())) {
                return i;
            }
        }
        return POSITION_NOT_FOUND;
    }

    String[] crack(String text, int keylen) {
        return new String[keylen];
    }

    boolean verify(String plain, String encruypted, String key) {
        return true;
    }

    static char[][] createVigenereTable(String alphabet) {

        char[][] vigenereTable = new char[26][26];

        for (int c = 0; c < alphabet.length(); c++) {
            for (int r = 0; r < alphabet.length(); r++) {
                vigenereTable[c][r] = alphabet.charAt(r);
            }
            alphabet = exchangeAlphabet(alphabet);
        }
        return vigenereTable;
    }

}
