package com.example.tcpclient;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
 
public class MyActivity extends Activity
{
    private ListView mList;
    private ArrayList arrayList;
    private MyCustomAdapter mAdapter;
    private TCPClient mTcpClient;
 
    private static MyActivity instance;
    
    public static MyActivity getInstance() {
    	return instance;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
 
        arrayList = new ArrayList();
 
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setTextColor(Color.MAGENTA);
        Button send = (Button)findViewById(R.id.send_button);
        Button pictures = (Button)findViewById(R.id.pictures_button);
        //relate the listView from java to the one created in xml
        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);
 
        // connect to the server
        new connectTask().execute("");
        pictures.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mTcpClient != null) {
                    mTcpClient.sendMessage("getpictures");
                }
				
//				Intent i = new Intent(getApplicationContext(), PicturesActivity.class);
//				startActivity(i);
			}
		});
        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	editText.setTextColor(Color.MAGENTA);
                String message = editText.getText().toString();
               
                //add the text in the arrayList
                arrayList.add("c: " + message);
 
                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
 
                //refresh the list
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
 
        instance = this;
    }
 
    public class connectTask extends AsyncTask<String,String,TCPClient> {
 
        @Override
        protected TCPClient doInBackground(String... message) {
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                	if(message.startsWith("sendfile") || message.startsWith("getpictures") || message.startsWith("GET_PICTURES") )
                		return;
                    publishProgress(message);
                    
                    if (mTcpClient.sendPictures) {
                    	mTcpClient.sendPictures = false;
                    	Intent i = new Intent(getApplicationContext(), PicturesActivity.class);
        				startActivity(i);
                    }
                }
            });
            mTcpClient.run();
 
            return null;
        }
 
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            
            //in the arrayList we add the messaged received from server
           
            arrayList.addAll(Arrays.asList(values));
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }
    
}