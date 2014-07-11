package com.example.tcpclient;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(PicturesActivity.this, "Image " + position,
	                    Toast.LENGTH_SHORT).show();
				ImageView image = (ImageView) MyActivity.getInstance().findViewById(R.id.superview);
				 InputStream open = null;
					try {
					      open = PicturesActivity.manager.open(TCPClient.getPicturesHashMap().get(position));
					      Bitmap bitmap = BitmapFactory.decodeStream(open);
					      // Assign the bitmap to an ImageView in this layout
					      image.setImageBitmap(bitmap);
					    } catch (IOException e) {
					      e.printStackTrace();
					    } finally {
					      if (open != null) {
					        try {
					          open.close();
					        } catch (IOException e) {
					          e.printStackTrace();
					        }
					      }
					    } 
	            //Intent intent = new Intent(MyActivity.getInstance(), MyActivity.class);
	            //startActivity(intent);
			}
		});
	}
}
