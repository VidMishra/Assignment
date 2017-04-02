package com.thb.vidyanand.assignmentthb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import Util.Constants;
import database.DataSource;
import models.UserDetailsModel;
import services.LoadDisplayImg;

/**
 * Created by vidyanand on 13/8/15.
 */
public class ProfileDetailsActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String CLASS_TAG = ProfileDetailsActivity.class.getSimpleName();

	private DataSource mDataSource;
	private EditText mEtName;
	private EditText mEtEmail;
	private EditText mEtBirthday;
	private EditText mEtAboutMe;
	private EditText mEtGender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_details);

		Intent intent = getIntent();

		if(intent != null) {
			String personName = intent.getStringExtra(Constants.INTENT_KEY_PERSON_NAME);
			String personEmail = intent.getStringExtra(Constants.INTENT_KEY_PERSON_EMAIL);
			String personDpUrl = intent.getStringExtra(Constants.INTENT_KEY_PERSON_DP_URL);
			String personDOB = intent.getStringExtra(Constants.INTENT_KEY_PERSON_DOB);
			String personAboutMe = intent.getStringExtra(Constants.INTENT_KEY_PERSON_ABOUT_ME);

			int personGender = intent.getIntExtra(Constants.INTENT_KEY_PERSON_GENDER, -1);
			String gender;

			if(personGender == 1) {
				gender = getString(R.string.female);
			} else if(personGender == 0) {
				gender = getString(R.string.male);
			} else {
				gender = getString(R.string.not_found);
			}

			Log.i(CLASS_TAG, "Name: " + personName + ", About Me: " + personAboutMe + ", Email: " + personEmail
					+ ", DP URL: " + personDpUrl + ", DOB: " + personDOB + ", Gender: " + gender);

			if(!DataSource.isInsertedData) {
				insertDataIntoLocalDB(personName, personEmail, personDOB, personAboutMe, gender);
			}
			initViews(personDpUrl);
		}
	}

	/**
	 * Inserting Data into Local DB
	 * @param name
	 * @param email
	 * @param dob
	 * @param aboutMe
	 * @param gender
	 */
	private void insertDataIntoLocalDB(String name, String email, String dob, String aboutMe, String gender) {

		UserDetailsModel userDetailsModel = new UserDetailsModel();

		if(name != null) {
			userDetailsModel.setUserName(name);
		} else {
			userDetailsModel.setUserName(getString(R.string.not_found));
		}

		if(email != null) {
			userDetailsModel.setEmail(email);
		} else {
			userDetailsModel.setEmail(getString(R.string.not_found));
		}

		if(dob != null) {
			userDetailsModel.setBirthday(dob);
		} else {
			userDetailsModel.setBirthday(getString(R.string.not_found));
		}

		if(aboutMe != null) {
			userDetailsModel.setAboutMe(aboutMe);
		} else {
			userDetailsModel.setAboutMe(getString(R.string.not_found));
		}

		if(gender != null) {
			userDetailsModel.setGender(gender);
		}

		mDataSource = new DataSource(this);
		mDataSource.open();
		mDataSource.insertUserDetailsToLocalDB(userDetailsModel);
		mDataSource.close();
	}

	/**
	 * Initializing views
	 */
	private void initViews(String dpUrl) {

		if(dpUrl != null) {
			ImageView ivDisply = (ImageView) findViewById(R.id.iv_display_image);
			new LoadDisplayImg(ivDisply).execute(dpUrl);
		}

		mDataSource.open();
		UserDetailsModel userDetailsModel = mDataSource.getUserDetailsFromLocalDB();
		mDataSource.close();

		if(userDetailsModel == null) return;

		mEtName = (EditText) findViewById(R.id.et_name);
		mEtName.setText(userDetailsModel.getUserName());

		mEtEmail = (EditText) findViewById(R.id.et_email);
		mEtEmail.setText(userDetailsModel.getEmail());

		mEtBirthday = (EditText) findViewById(R.id.et_dob);
		mEtBirthday.setText(userDetailsModel.getBirthday());

		mEtAboutMe = (EditText) findViewById(R.id.et_about_me);
		mEtAboutMe.setText(userDetailsModel.getAboutMe());

		mEtGender = (EditText) findViewById(R.id.et_gender);
		mEtGender.setText(userDetailsModel.getGender());

		Button btnUpdate = (Button) findViewById(R.id.btn_update);
		btnUpdate.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu_profile_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent;

		switch (item.getItemId()) {
			case R.id.action_screen2:
				intent = new Intent(this, SwipeRefresh.class);
				startActivity(intent);
				return true;
			case R.id.action_screen3:
				intent = new Intent(this, MapActivity.class);
				startActivity(intent);
				return true;
			case R.id.action_screen4:
				intent = new Intent(this, ImageUploadActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.btn_update:
				mDataSource.open();
				mDataSource.deleteAllUserDetails();
				mDataSource.close();
				insertDataIntoLocalDB(mEtName.getText().toString(), mEtEmail.getText().toString(), mEtBirthday.getText().toString(),
						mEtAboutMe.getText().toString(), mEtGender.getText().toString());
				break;
		}
	}
}
