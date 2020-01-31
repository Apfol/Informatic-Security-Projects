/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author apfol
 */
public class BreakingVigenere {

    final static int POSITION_NOT_FOUND = 30;

    public static void main(String[] args) throws FileNotFoundException, IOException {

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
                String[] possibleKeys = crack(encrypted, keyLength);
                System.out.println("Posibles claves: ");
                for(String k: possibleKeys) {
                    System.out.println(k);
                }
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

    static String[] crack(String text, int keylen) throws FileNotFoundException, IOException {

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
        ArrayList<Map<String, Integer>> subcryptogramsFrecuencies = new ArrayList<>();

        //Obtener frecuencias
        for (int sub = 0; sub < subcryptograms.length; sub++) {
            subcryptogramsFrecuencies.add(new HashMap<>());
            for (int characterPosition = 0; characterPosition < alphabet.length(); characterPosition++) {
                int frecuencyCounter = 0;
                for (int encryptedPosition = 0; encryptedPosition < subcryptograms[sub].length(); encryptedPosition++) {
                    if (String.valueOf(alphabet.charAt(characterPosition)).toLowerCase().equals(String.valueOf(subcryptograms[sub].charAt(encryptedPosition)).toLowerCase())) {
                        frecuencyCounter++;
                    }
                }
                subcryptogramsFrecuencies.get(sub).put(String.valueOf(alphabet.charAt(characterPosition)), frecuencyCounter);
            }
        }

        //Ordernar frecuencias
        ArrayList<Map<String, Integer>> subcryptogramsFrecuenciesOrdered = new ArrayList<>();
        for (int sub = 0; sub < subcryptograms.length; sub++) {
            Map<String, Integer> sorted = sortByValue(subcryptogramsFrecuencies.get(sub));
            subcryptogramsFrecuenciesOrdered.add(sorted);
        }

        //Obtener letras que conforman la clave
        List<List<String>> posiblesKeysLetters = new ArrayList<>();
        for (int sub = 0; sub < subcryptogramsFrecuenciesOrdered.size(); sub++) {
            Map<String, Integer> actualHash = subcryptogramsFrecuenciesOrdered.get(sub);
            Integer[] frecuencies = actualHash.values().toArray(new Integer[subcryptogramsFrecuenciesOrdered.get(sub).size()]);
            int firstHighFrecuency = frecuencies[frecuencies.length - 1];
            posiblesKeysLetters.add(new ArrayList<>());
            for (int character = 0; character < alphabet.length(); character++) {
                int ePosition = character + 4;
                int oPosition = character + 4 + 10;

                if (ePosition > alphabet.length() - 1) {
                    ePosition = ePosition - (alphabet.length() - 1) - 1;
                }
                if (oPosition > alphabet.length() - 1) {
                    oPosition = oPosition - (alphabet.length() - 1) - 1;
                }
                String AEO = String.valueOf(alphabet.charAt(character)) + String.valueOf(alphabet.charAt(ePosition)) + String.valueOf(alphabet.charAt(oPosition));
                if (actualHash.get(String.valueOf(AEO.charAt(0))) == firstHighFrecuency || actualHash.get(String.valueOf(AEO.charAt(1))) == firstHighFrecuency || actualHash.get(String.valueOf(AEO.charAt(2))) == firstHighFrecuency) {
                    posiblesKeysLetters.get(sub).add(String.valueOf(AEO.charAt(0)));
                }
            }
        }

        //Obtener posibles claves en base del diccionario
        File dictionary = new File("spanish.txt");
        BufferedReader br = new BufferedReader(new FileReader(dictionary));
        List<String> possibleKeys = new ArrayList<>();
        String st;
        while ((st = br.readLine()) != null) {
            int counterLetters = 0;
            if (st.length() == keylen) {
                for (int i = 0; i < st.length(); i++) {
                    boolean exitForLetters = false;
                    for (int j = 0; j < posiblesKeysLetters.get(i).size(); j++) {
                        if (!exitForLetters) {
                            if (posiblesKeysLetters.get(i).get(j).toLowerCase().equals(String.valueOf(st.charAt(i)).toLowerCase())) {
                                exitForLetters = true;
                                counterLetters++;
                            }
                        }
                    }
                }
            }
            if (counterLetters == keylen) {
                possibleKeys.add(st);
            }
        }
        String[] possibleKeysArray = possibleKeys.toArray(new String[possibleKeys.size()]);
        return possibleKeysArray;
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        List<Map.Entry<String, Integer>> list
                = new LinkedList<>(unsortMap.entrySet());

        Collections.sort(list, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> (o1.getValue()).compareTo(o2.getValue()));
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        list.forEach((entry) -> {
            sortedMap.put(entry.getKey(), entry.getValue());
        });
        return sortedMap;
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
