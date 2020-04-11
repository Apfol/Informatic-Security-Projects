
package com.mycompany.homework3;

import java.awt.HeadlessException;
import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

/**
 * Class Cliente works as an socket client with capacity to connect to a server providing the ip address, also works has functions like send and receive data(files and text)
 *
 * @author Andres Ramos
 * @author Carlos Gutierrez
 * @version 1.0
 * @see CllientHandler
 */
public class Cliente {
    
    // initialize socket and input output streams 

    public final static int FILE_SIZE = 90505;
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    /**
     * Method (Constructor) where the socket is created with the port 5056 and local ip host, then wait for connections and send them to a class extended from socket threads
     *
     * @param pathtosave String with the public file path to be receive and stored
     * @param pathfileencrypted String with the path where the encrypted file is  stored
     * @see FileShare
     * 
     * 
     */
    public Cliente( String pathtosave, String pathfileencrypted) {
        
        try {

           

            // getting localhost ip 
            String ip = JOptionPane.showInputDialog("Ingrese la ip a la que desea conectarse ","localhost");

            // establish the connection with server port 5056 
            Socket s = new Socket(ip, 5056);

            // obtaining input and out streams 
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
           

            // the following loop performs the exchange of 
            // information between client and client handler 
            while (true) {
                String tosend=JOptionPane.showInputDialog(dis.readUTF());
                

                dos.writeUTF(tosend);

                // If client sends exit,close this connection  
                // and then break from the while loop 
                if (tosend.equals("1")) {
                    dos.writeUTF(tosend);
                    try {
                        System.out.println("Entro");

                        byte[] mybytearray = new byte[FILE_SIZE];
                        InputStream is = s.getInputStream();
                        fos = new FileOutputStream(pathtosave);
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
                        JOptionPane.showMessageDialog(null, "File " + pathtosave
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
                  
                    
                    break;
                } else if (tosend.equals("2")) {
                    FileInputStream fis = null;
                    BufferedInputStream bis = null;
                    OutputStream os = null;
                    try {
                        String ext = ".txt";
                        ext="."+JOptionPane.showInputDialog(null,"Ingrese la extension del archivo sin punto(txt)");
                        dos.writeUTF(ext);
                        File myFile = new File(pathfileencrypted+ext);
                        System.out.println(pathfileencrypted+ext);
                        byte[] mybytearray = new byte[(int) myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray, 0, mybytearray.length);
                        os = s.getOutputStream();
                        System.out.println("Sending " + pathfileencrypted+ext + "(" + mybytearray.length + " bytes)");
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

                }
                if (tosend.equals("Exit")) {

                    break;
                }

               
                String received = dis.readUTF();
                System.out.println(received);
            }

     
            dis.close();
            dos.close();
        } catch (HeadlessException | IOException e) {
        }
    }

}
