package com.example.tcpclient;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.GridView;

public class PicturesActivity extends Activity {

	private static PicturesActivity instance;
	public static AssetManager manager;
	public static PicturesActivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pictures_my);
		manager = manager = getAssets();
		GridView gridview = (GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(new ImageAdapter(getApplicationContext(), TCPClient.getPicturesHashMap()));
		
	}
}
