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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
 
public class TCPClient {
 
    private String serverMessage;
    public static final String SERVERIP = "192.168.95.1"; //your computer IP address
    public static final int SERVERPORT = 4441;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
 
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
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                	if (toReceive) {
                		try {
	                		byte [] mybytearray ;
	                		int bytesRead;
	                		InputStream is = getInputStream();
	                		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                		
//	                		for (int i = 0; i < len; i++) {
//	                			byte byteToBeRead = (byte)is.read();
//	                			baos.write(byteToBeRead);
//	                			Log.d("FILE", "downloading " + (i * 100.0 / len) + "%");
//	                		}
//	                		mybytearray = baos.toByteArray();
	                		
	                		mybytearray = new byte[len];
//	                		bytesRead = is.read(mybytearray, 0, mybytearray.length);
	                		
	                		//File file = new File("res/newfile");
	                		//OutputStream fos = new FileOutputStream(file);
	                		
	                		int offset = 0;
	                		while(len > 0){
	                			Log.e("bla", "remaining " + len);
	                			bytesRead = is.read(mybytearray, offset, len);
	                			if(bytesRead == -1)
	                			{
	                				Log.e("bla", "jighkd");
	                				break;
	                			}
	                			//fos.write(mybytearray, offset, bytesRead);
	                			
	                			len = len - bytesRead;
	                			offset += bytesRead;
	                			Log.d("FILE", "downloaded " + len * 100.0 / mybytearray.length);
	                		}
	                		
	                		Log.d("FILE", "image downloaded");
	                		Bitmap bmp;
	                		bmp = BitmapFactory.decodeByteArray(mybytearray, 0, mybytearray.length);
	                		final Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
	                		
	                		final ImageView iv = (ImageView)MyActivity.getInstance().findViewById(R.id.superview);
	                		iv.post(new Runnable() {
	                			@Override
	                			public void run() {
	                				iv.setImageBitmap(mutableBitmap);
	                			}
	                		});
	                		
	                		
	                		Log.d("FILE", new String(mybytearray));
	                		len = 0;
	                		toReceive = false;
	                		
	                		continue;
                		} catch (IOException e) {
                			e.printStackTrace();
                		}
                		
                	} 

                	serverMessage = in.readLine();
                	
                	if(serverMessage.startsWith("SEND_FILE")) {
                		Log.d("FILE", "buffer = " + serverMessage.substring(9));
	                	len = Integer.parseInt(serverMessage.substring(9));
	                	toReceive = true;
	                	
	                	Log.d("FILE", "to receive pictures = " + len);
                	}
                	
                	if(serverMessage.startsWith("GET_PICTURES")) {
                		Log.d("PICTURE", "buffer = " + serverMessage.substring(12));
	                	nr = Integer.parseInt(serverMessage.substring(12));
	                	toReceivePictures = true;
	                	
//	                	Log.d("PICTURE", "to receive = " + nr);
                	}
                    if (serverMessage != null && mMessageListener != null && !serverMessage.startsWith("SEND_FILE")) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
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
        public abstract void messageReceived(String message);
    }
}