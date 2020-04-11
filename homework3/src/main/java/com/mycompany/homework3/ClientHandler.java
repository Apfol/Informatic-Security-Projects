/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework3;

import static com.mycompany.homework3.Cliente.FILE_SIZE;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author apolu
 */
public class ClientHandler extends Thread {

    final DataInputStream dis;
    boolean b = false;
    final DataOutputStream dos;
    String pathpublickey;
    String pathtosave;
    final Socket s;
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;

    // Constructor 
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, String pathpublickey, String pathtosaves) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.pathpublickey = pathpublickey;
        this.pathtosave = pathtosaves;
    }
    

    @Override
    public void run() {
        String received;
        
        while (true) {
            try {

                // Ask user what he wants 
                
                dos.writeUTF(   "Selecione un opcion: " + "\n"
                        + "1. Solicitar clave publica" + "\n"
                        + "2. Enviar archivo cifrado con clave publica");

                // receive the answer from client 
                received = dis.readUTF();
                switch (received) {
                    case "1":


                        try {
                        System.out.println("Entro a clienthandler");
                        File myFile = new File(pathpublickey);
                        System.out.println(pathpublickey);
                        byte[] mybytearray = new byte[(int) myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray, 0, mybytearray.length);
                        os = s.getOutputStream();
                        System.out.println("Sending " + pathpublickey + "(" + mybytearray.length + " bytes)");
                        os.write(mybytearray, 0, mybytearray.length);
                        os.flush();
                        System.out.println("Done.");
                    } finally {
                        if (bis != null) {
                            bis.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                        if (s != null) {
                            s.close();
                        }
                    }
                    
                    break;

                    case "2":
                        int FILE_SIZE = 90505;
                        int bytesRead;
                        int current = 0;
                        FileOutputStream fos = null;
                        BufferedOutputStream bos = null;
                        try {
                            System.out.println("Entro");

                            String ext = ".txt";
                            ext="."+dis.readUTF();
                            byte[] mybytearray = new byte[FILE_SIZE];
                            InputStream is = s.getInputStream();
                            fos = new FileOutputStream(pathtosave+ext);
                            bos = new BufferedOutputStream(fos);
                            bytesRead = is.read(mybytearray, 0, mybytearray.length);
                            current = bytesRead;

                            do {
                                bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
                                if (bytesRead >= 0) {
                                    current += bytesRead;
                                }
                            } while (bytesRead > -1);

                            bos.write(mybytearray, 0, current);
                            bos.flush();
                            JOptionPane.showMessageDialog(null, "File " + pathtosave+ext
                                    + " downloaded (" + current + " bytes read)");

                        } finally {
                            if (fos != null) {
                                fos.close();
                            }
                            if (bos != null) {
                                bos.close();
                            }
                            if (s != null) {
                                s.close();
                            }
                        }
                        b=true;
                        JOptionPane.showMessageDialog(null, "Se comenzara la desencriptacion del archivo recibido.");
                        FileShare scv = new FileShare();
                        scv.descifrarauto();
                        
                        break;

                    case "Exit":
                        System.out.println("Client " + this.s + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.s.close();
                        System.out.println("Connection closed");
                        break;
                    default:
                        System.out.println("Eleccion incorrecta");
                        break;
                }

            } catch (IOException e) {
            } catch (Exception ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
