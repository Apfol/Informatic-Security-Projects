/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.homework3;

import java.net.*; 
import java.io.*; 
import static java.sql.DriverManager.println;
import javax.swing.JOptionPane;
/**
 *
 * @author apolu
 */
public class Servidor {
     private Socket          socket   = null; 
    private ServerSocket    server   = null; 
    private DataInputStream in       =  null; 
    private DataOutputStream out = null;
    private DataInputStream input       =  null; 
    

    
  
    // constructor with port 
    public  Servidor(int port, String publicpath,String pathtosave) throws IOException   { 
         // starts server and waits for a connection 
       // server is listening on port 5056 
        ServerSocket ss = new ServerSocket(5056); 
          String publickeypath="";
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            Socket s = null; 
              
            try 
            { 
                // socket object to receive incoming client requests 
                s = ss.accept(); 
                  
                System.out.println("A new client is connected : " + s.getInetAddress()); 
                  
                // obtaining input and out streams 
                DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
                  
                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                Thread t = new ClientHandler(s, dis, dos,publicpath,pathtosave); 
  
                // Invoking the start() method 
                t.start(); 
                  
            } 
            catch (Exception e){ 
                s.close(); 
                e.printStackTrace(); 
            } 
        } 
    }
  
   
    
    
}
