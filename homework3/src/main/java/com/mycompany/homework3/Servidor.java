package com.mycompany.homework3;

import java.io.*;
import java.net.*;

/**
 * Class Servidor works as an socket server with capacity to manage socket threads
 *
 * @author Andres Ramos
 * @author Carlos Gutierrez
 * @version 1.0
 * @see ClientHandler
 */
public class Servidor {

  /**
   * Method where the socket is created with the port 5056 and local ip host, then wait for
   * connections and send them to a class extended from socket threads
   *
   * @param publicpath String with the public file path to be sent
   * @param pathtosave String with the path where the encrypted file would be stored
   * @see FileShare
   * @throws IOException Throws an exception when the main process does not finish as expected
   * @author Geeksforgeeks Review
   *     https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/ All the
   *     instructions between the while loop where taker from the link referenced
   */
  public Servidor(String publicpath, String pathtosave) throws IOException {
    // starts server and waits for a connection
    // server is listening on port 5056
    ServerSocket ss = new ServerSocket(5056);

    // running infinite loop for getting
    // client request
    while (true) {
      Socket s = null;

      try {
        // socket object to receive incoming client requests
        s = ss.accept();

        System.out.println("A new client is connected : " + s.getInetAddress());

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        System.out.println("Assigning new thread for this client");

        // create a new thread object
        Thread t = new ClientHandler(s, dis, dos, publicpath, pathtosave);

        // Invoking the start() method
        t.start();

      } catch (Exception e) {
        s.close();
        e.printStackTrace();
      }
    }
  }
}
