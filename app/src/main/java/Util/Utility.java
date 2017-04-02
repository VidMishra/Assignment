package Util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vidyanand on 15/8/15.
 */
public class Utility {

	private static final String CLASS_TAG = Utility.class.getSimpleName();

	/**
	 * Get bitmap from uri path
	 * @param path
	 * @param context
	 * @return bitmap
	 */
	public static Bitmap getBitmap(String path, Context context) {

		Uri uri = Uri.fromFile(new File(path));
		InputStream in;
		ContentResolver mContentResolver = context.getContentResolver();

		try {
			final int IMAGE_MAX_SIZE = 1024;
			in = mContentResolver.openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
			}

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			in = mContentResolver.openInputStream(uri);
			Bitmap b = BitmapFactory.decodeStream(in, null, o2);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			b.compress(Bitmap.CompressFormat.JPEG, 25, stream);
			in.close();
			return getBnWImage(b);
		}
		catch (FileNotFoundException e) {
			Log.e(CLASS_TAG, "file " + path + " not found");
		}
		catch (IOException e) {
			Log.e(CLASS_TAG, "file " + path + " not found");
		}
		return null;
	}

	/**
	 * Converting image into black and white preview
	 * @param bitmap
	 * @return bitmap B&W
	 */
	public static Bitmap getBnWImage(Bitmap bitmap) {

		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);

		Bitmap bNwBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		Paint paint = new Paint();
		paint.setColorFilter(colorMatrixFilter);

		Canvas canvas = new Canvas(bNwBitmap);
		canvas.drawBitmap(bNwBitmap, 0, 0, paint);
		return bNwBitmap;
	}

	/**
	 * Generating time stamp to make default image name
	 * @return timeStamp
	 */
	public static String getTimeStamp() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddhhmmss");
		String timeStamp = simpleDateFormat.format(new Date());
		return timeStamp;
	}
}
