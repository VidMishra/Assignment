package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Util.Constants;

/**
 * Created by vidyanand on 15/8/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String CREATE_USER_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.USER_DETAILS_TABLE
			+ "(" + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ Constants.USER_NAME + " VARCHAR,"
			+ Constants.USER_GENDER + " VARCHAR,"
			+ Constants.USER_BIRTHDAY + " VARCHAR,"
			+ Constants.USER_EMAIL + " VARCHAR,"
			+ Constants.USER_ABOUT_ME + " VARCHAR);";

	private static final String CREATE_IMAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.IMAGE_URI_TABLE
			+ "(" + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ Constants.IMAGE_URI + " VARCHAR);";

	public DatabaseHandler(Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER_DETAILS_TABLE);
		db.execSQL(CREATE_IMAGE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}
}
