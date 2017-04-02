package services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by vidyanand on 13/8/15.
 */
public class LoadDisplayImg extends AsyncTask<String, Void, Bitmap> {
	ImageView mIvDisplayImg;

	public LoadDisplayImg(ImageView ivDisplayImg) {
		this.mIvDisplayImg = ivDisplayImg;
	}

	protected Bitmap doInBackground(String... urls) {
		String urlDisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urlDisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		mIvDisplayImg.setImageBitmap(result);
	}
}
