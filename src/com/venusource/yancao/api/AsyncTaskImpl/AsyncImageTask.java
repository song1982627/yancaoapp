package com.venusource.yancao.api.AsyncTaskImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class AsyncImageTask extends AsyncTask<Object, Object, Object>{
	private ImageView imageView;
	private String imageUrl;
	
	public AsyncImageTask(ImageView imageView,String imageUrl) {
		// 设定参数
		this.imageView = imageView;
		this.imageUrl = imageUrl;
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		if (imageView != null && imageUrl != null) {
			Bitmap bmp = null;
			URL url = null;
			HttpURLConnection conn = null;
			InputStream stream = null;
			try {
				url = new URL(imageUrl);
				conn = (HttpURLConnection) url.openConnection(); 
				conn.setDoInput(true); 
				conn.connect();
				stream = conn.getInputStream();
				bmp = BitmapFactory.decodeStream(stream); 				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					stream.close();
					conn.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			return bmp;
			
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if (result != null) {
			// 此处更新图片
			this.imageView.setImageBitmap((Bitmap) result);

		}
	}
}
