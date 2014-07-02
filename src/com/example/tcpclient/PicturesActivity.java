package com.example.tcpclient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.GridView;

public class PicturesActivity extends Activity {

	private static PicturesActivity instance;

	public static PicturesActivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pictures_my);
		GridView gridview = (GridView) findViewById(R.id.gridView1);

	}
}
