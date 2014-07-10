package com.example.tcpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private HashMap<Integer, String> hashMap = new HashMap<>();
	public ImageAdapter(Context c, HashMap<Integer, String> hashMap) {
		mContext = c;
		this.hashMap = hashMap;
	}

	public int getCount() {
		return hashMap.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}
		 
		 InputStream open = null;
		try {
		      open = PicturesActivity.manager.open(hashMap.get(position));
		      Bitmap bitmap = BitmapFactory.decodeStream(open);
		      // Assign the bitmap to an ImageView in this layout
		      imageView.setImageBitmap(bitmap);
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
		return imageView;
	}
}