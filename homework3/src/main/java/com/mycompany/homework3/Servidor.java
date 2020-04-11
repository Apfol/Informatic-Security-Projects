/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework3;

import java.io.*;
import java.net.*;

/** @author apolu */
public class Servidor {

  // constructor with port
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
