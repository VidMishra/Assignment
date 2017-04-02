package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.thb.vidyanand.assignmentthb.R;

import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import models.ImageUriModel;
import models.UserDetailsModel;

/**
 * Created by vidyanand on 15/8/15.
 */
public class DataSource {

	private static final String CLASS_TAG = DataSource.class.getSimpleName();

	public static boolean isInsertedData;
	public SQLiteDatabase mSQLiteDB;
	public DatabaseHandler mDbHandler;
	private Context mContext;

	public DataSource(Context context) {
		mDbHandler = new DatabaseHandler(context);
		this.mContext = context;
	}

	public void open() throws SQLException {
		mSQLiteDB = mDbHandler.getWritableDatabase();
	}

	public void close() {
		mDbHandler.close();
	}

	/**
	 * Inserting data into local DB
	 * @param userDetailsModel
	 */
	public void insertUserDetailsToLocalDB(UserDetailsModel userDetailsModel) {
		try {
			ContentValues values = new ContentValues();
			values.put(Constants.USER_NAME, userDetailsModel.getUserName());
			values.put(Constants.USER_GENDER, userDetailsModel.getGender());
			values.put(Constants.USER_BIRTHDAY, userDetailsModel.getBirthday());
			values.put(Constants.USER_EMAIL, userDetailsModel.getEmail());
			values.put(Constants.USER_ABOUT_ME, userDetailsModel.getAboutMe());

			long rowId = mSQLiteDB.insert(Constants.USER_DETAILS_TABLE, null, values);
			Log.i(CLASS_TAG, "Row Inserted: " + rowId);
			isInsertedData = true;
			Toast.makeText(mContext, mContext.getString(R.string.inserted_successfully), Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get user details from local DB
	 * @return userDetailsModel
	 */
	public UserDetailsModel getUserDetailsFromLocalDB() {
		UserDetailsModel userDetailsModel = new UserDetailsModel();
		Cursor cursor = mSQLiteDB.query(Constants.USER_DETAILS_TABLE, null, null, null, null, null, null);
		if(cursor != null) {
			while (cursor.moveToNext()) {
				userDetailsModel.setUserName(cursor.getString(cursor.getColumnIndex(Constants.USER_NAME)));
				userDetailsModel.setGender(cursor.getString(cursor.getColumnIndex(Constants.USER_GENDER)));
				userDetailsModel.setBirthday(cursor.getString(cursor.getColumnIndex(Constants.USER_BIRTHDAY)));
				userDetailsModel.setEmail(cursor.getString(cursor.getColumnIndex(Constants.USER_EMAIL)));
				userDetailsModel.setAboutMe(cursor.getString(cursor.getColumnIndex(Constants.USER_ABOUT_ME)));
			}
		}
		return userDetailsModel;
	}

	/**
	 * Inserting image uri into local DB
	 * @param imageUriModel
	 */
	public void insertImageUriToLocalDB(ImageUriModel imageUriModel) {
		try {
			ContentValues values = new ContentValues();
			values.put(Constants.IMAGE_URI, imageUriModel.getImageUri());
			long rowId = mSQLiteDB.insert(Constants.IMAGE_URI_TABLE, null, values);
			Log.i(CLASS_TAG, "Row Inserted in URI table: " + rowId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get image uri from local DB
	 * @return imageUriModelList
	 */
	public List<ImageUriModel> getImageUriFromLocalDB() {
		List<ImageUriModel> imageUriModelList = new ArrayList<>();
		Cursor cursor = mSQLiteDB.query(Constants.IMAGE_URI_TABLE, null, null, null, null, null, null);
		if(cursor != null) {
			while (cursor.moveToNext()) {
				ImageUriModel imageUriModel = new ImageUriModel();
				imageUriModel.setImageUri(cursor.getString(cursor.getColumnIndex(Constants.IMAGE_URI)));
				imageUriModelList.add(imageUriModel);
			}
		}
		return imageUriModelList;
	}

	/**
	 * delete all record of user details table
	 */
	public void deleteAllUserDetails() {
		int rowDeleted = mSQLiteDB.delete(Constants.USER_DETAILS_TABLE, null, null);
		Log.i(CLASS_TAG, "Deleted row: "+rowDeleted);
	}
}
