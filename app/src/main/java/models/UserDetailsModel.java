package models;

/**
 * Created by vidyanand on 15/8/15.
 */
public class UserDetailsModel {

	private String mUserName;
	private String mGender;
	private String mBirthday;
	private String mEmail;
	private String mAboutMe;

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}

	public String getGender() {
		return mGender;
	}

	public void setGender(String mGender) {
		this.mGender = mGender;
	}

	public String getBirthday() {
		return mBirthday;
	}

	public void setBirthday(String mBirthday) {
		this.mBirthday = mBirthday;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getAboutMe() {
		return mAboutMe;
	}

	public void setAboutMe(String mAboutMe) {
		this.mAboutMe = mAboutMe;
	}
}
