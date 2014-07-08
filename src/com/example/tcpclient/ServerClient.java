package com.example.tcpclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;


public class ServerClient extends Thread{
	public Integer port;
	private boolean running = false;
	Socket client;
	private PrintWriter mOut;
	private OnMessageReceived messageListener;
	public ServerSocket serverSocket;
	public ServerClient(Integer port) {
		// TODO Auto-generated constructor rstub
		this.port = port;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		running = true;
		 
        try {
            System.out.println("SC: Connecting...");
 
            //create a server socket. A server socket waits for requests to come in over the network.
            serverSocket = new ServerSocket(port);
 
            //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
            client = serverSocket.accept();
            
            Log.d("MobileSocket", "SC: Receiving...");
 
            try {
 
                //sends the message to the client
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
 
                //read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
 
                //in this while we wait to receive messages from client (it's an infinite loop)
                //this while it's like a listener for messages
                while (running) {
                    String message = in.readLine();
 
                    if (message != null && messageListener != null) {
                        //call the method messageReceived from ServerBoard class
                        messageListener.messageReceived(message);
                    }
                }
 
            } catch (Exception e) {
                System.out.println("S: Error");
                e.printStackTrace();
            } finally {
                client.close();
                System.out.println("S: Done.");
            }
 
        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }
	}
	public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
