package com.example.tcpclient;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.acl.LastOwnerException;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
 
public class TCPClient {
 
    private String serverMessage;
    public static final String SERVERIP = "192.168.95.1"; //your computer IP address
    public static final int SERVERPORT = 4444;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    public boolean sendPictures = false;
    public ServerClient serverClient;
    private static HashMap<Integer, String> picturesHashMap = new HashMap<>();
    PrintWriter out;
    BufferedReader in;
    Socket socket;
    
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }
 
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public static HashMap<Integer, String> getPicturesHashMap() {
		return picturesHashMap;
	}
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }
 
    public void stopClient(){
        mRun = false;
    }
 
    public void run() {
 
        mRun = true;
 
        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            serverClient = new ServerClient(SERVERPORT);
    		serverClient.start();
            Log.e("TCP Client", "C: Connecting...");
 
            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVERPORT);
 
            try {
                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                Log.e("TCP Client", "C: Sent.");
                Log.e("TCP Client", "C: Done.");
                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                boolean toReceive = false;
                boolean toReceivePictures = false;
                int len = 0;
                int nr = 0;
                int position = 0;
                //in this while the client listens for the messages sent by the server
                while (mRun) {

                	serverMessage = in.readLine();
                	Log.d("Server: ", serverMessage);
                	if(serverMessage.startsWith("sendimage")){
                		picturesHashMap.put(position, serverMessage.substring(10,21));
                		Log.d("HASH", serverMessage.substring(10,21) + "  " + position +"  " + serverMessage.substring(21));
                		position++;
                	}
                	
                	if(nr == (position + 1)){
                		sendPictures = true;
                	}
                	
                	if(serverMessage.startsWith("GET_PICTURES")) {
                		Log.d("PICTURE", "buffer = " + serverMessage.substring(12));
	                	nr = Integer.parseInt(serverMessage.substring(12));
	                	toReceivePictures = true;
	                	position = 0 ;
                	}
                    
                	if (serverMessage != null && mMessageListener != null && !serverMessage.startsWith("SEND_FILE") ) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                        Log.d("Receive", serverMessage);
                    }
                    serverMessage = null;
                }
 
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
            } catch (IOException e) {
 
                Log.e("TCP", "S: Error", e);
 
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
 
        } catch (Exception e) {
 
            Log.e("TCP", "C: Error", e);
 
        }
    }
 
    public InputStream getInputStream() throws IOException {
    	return socket.getInputStream();
    }
    
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public static abstract class OnMessageReceived {
    	boolean toReceive = false;
    	boolean toReceicePictures = false;
    	int len = 0;
    	int nr = 0;
    	int position = 0;
        public abstract void messageReceived(String message);
    }
}