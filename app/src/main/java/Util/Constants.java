package Util;

import android.os.Environment;

import java.io.File;

/**
 * Created by vidyanand on 13/8/15.
 */
public class Constants {

	public static final String INTENT_KEY_PERSON_NAME = "personName";
	public static final String INTENT_KEY_PERSON_DP_URL = "dpUrl";
	public static final String INTENT_KEY_PERSON_ABOUT_ME = "aboutMe";
	public static final String INTENT_KEY_PERSON_DOB = "dob";
	public static final String INTENT_KEY_PERSON_GENDER = "gender";
	public static final String INTENT_KEY_PERSON_EMAIL= "email";

	// DB constants
	public static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + File.separator +
			"AssignmentTHB" + File.separator + "AssignmentTHB.db";
	public static final int DATABASE_VERSION = 1;

	public static final String USER_DETAILS_TABLE = "UserDetails";
	public static final String IMAGE_URI_TABLE = "ImageUriTable";

	public static final String ID = "_id";
	public static final String USER_NAME = "_userName";
	public static final String USER_GENDER = "_userGender";
	public static final String USER_BIRTHDAY = "_userBirthday";
	public static final String USER_EMAIL = "_email";
	public static final String USER_ABOUT_ME = "_aboutMe";

	public static final String IMAGE_URI = "_ImageUri";
}
