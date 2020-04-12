package com.mycompany.homework3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Class ClientHandler works as an transitional socket, handle all the incoming request from the
 * server, using socket threads.
 *
 * @author Andres Ramos
 * @author Carlos Gutierrez
 * @version 1.0
 * @see Servidor
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

  /**
   * @param s
   * @param dis
   * @param dos
   * @param pathpublickey
   * @param pathtosaves
   */
  public ClientHandler(
      Socket s,
      DataInputStream dis,
      DataOutputStream dos,
      String pathpublickey,
      String pathtosaves) {
    this.s = s;
    this.dis = dis;
    this.dos = dos;
    this.pathpublickey = pathpublickey;
    this.pathtosave = pathtosaves;
  }

  /** */
  @Override
  /**
   * Method run works as an starter method, setting up all the interaction between client and server
   */
  public void run() {
    String received;

    while (true) {
      try {

        // Ask user what he wants
        dos.writeUTF(
            "Selecione un opcion: "
                + "\n"
                + "1. Solicitar clave publica"
                + "\n"
                + "2. Enviar archivo cifrado con clave publica");

        // receive the answer from client
        received = dis.readUTF();
        switch (received) {
          case "1":
            // Start the class to send the public key file to the client
            enviarclavepublica();
            break;

          case "2":
            // Start the class to receive the encrpyted file, then
            // decrpyt it
            recibirarchivocifradoydescifrar();
            break;

          case "Exit":
            // On case of exit close the socket  connection
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

  /**
   * Method to send the public key file from the server to the client. The path where the public key
   * file, should be the keys folder
   *
   * @throws FileNotFoundException Throws an exception where an specific path file to open is not
   *     found
   * @throws IOException Throws an exception when the main process does not finish as expected
   *     <p>Review https://www.rgagnon.com/javadetails/java-0542.html
   * @author Real Gagnon. Just the file sender operation
   */
  public void enviarclavepublica() throws FileNotFoundException, IOException {

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
  }

  /**
   * Method to receive the encrypted file sent from the client, this encrypted file must be
   * encrypted with the public key sent from the server. Also then the file is stored into encrypt
   * folder it start to decrypt with the private key of the server
   *
   * @throws FileNotFoundException Throws an exception where an specific path file to open is not
   *     found
   * @throws IOException Throws an exception when the main process does not finish as expected
   *     <p>Review https://www.rgagnon.com/javadetails/java-0542.html
   * @author Real Gagnon. Just the file sender operation
   */
  public void recibirarchivocifradoydescifrar()
      throws FileNotFoundException, IOException, Exception {
    int FILE_SIZE = 90505;
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    try {
      System.out.println("Entro");

      String ext = ".txt";
      ext = "." + dis.readUTF();
      byte[] mybytearray = new byte[FILE_SIZE];
      InputStream is = s.getInputStream();
      fos = new FileOutputStream(pathtosave + ext);
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
      JOptionPane.showMessageDialog(
          null, "File " + pathtosave + ext + " downloaded (" + current + " bytes read)");

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
    b = true;
    JOptionPane.showMessageDialog(null, "Se comenzara la desencriptacion del archivo recibido.");
    FileShare scv = new FileShare();
    scv.descifrarauto();
  }
}
