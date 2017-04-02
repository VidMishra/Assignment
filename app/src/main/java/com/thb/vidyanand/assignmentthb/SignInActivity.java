package com.thb.vidyanand.assignmentthb;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import Util.Constants;

/**
 * Created by vidyanand on 13/8/15.
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private static final String CLASS_TAG = SignInActivity.class.getSimpleName();
	private static final int SIGN_IN_REQUEST_CODE = 0;

	private static final int DP_PIXEL_SIZE = 500;
	private boolean isIntentInProgress;
	private boolean isSignedIn;
	private GoogleApiClient mGoogleApiClient;
	private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		Button btnSignIn = (Button) findViewById(R.id.btn_screen1);
		btnSignIn.setOnClickListener(this);

		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
		mGoogleApiClient.connect();
	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Once click on signIn button then it will handle the error until user will get signed in.
	 */
	private void handleSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				isIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, SIGN_IN_REQUEST_CODE);
			} catch (IntentSender.SendIntentException e) {
				isIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
			return;
		}

		if (!isIntentInProgress) {
			// Storing ConnectionResult for further use
			mConnectionResult = result;

			if (isSignedIn) {
				handleSignInError();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {

		if (requestCode == SIGN_IN_REQUEST_CODE) {
			if (responseCode != RESULT_OK) {
				isSignedIn = false;
			}
			isIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		isSignedIn = false;
		getGoogleplusInfo();
	}

	/**
	 * Getting G+ info name, email, dp
	 */
	private void getGoogleplusInfo() {

		Intent intent;

		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person personLoggedIn = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

				String personName = personLoggedIn.getDisplayName();
				String personDpUrl = personLoggedIn.getImage().getUrl();
				String personAboutMe = personLoggedIn.getAboutMe();
				String personDOB = personLoggedIn.getBirthday();
				String personEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);

				int personGender = personLoggedIn.getGender();

				Log.i(CLASS_TAG, "Name: " + personName + ", About Me: " + personAboutMe + ", Email: " + personEmail
						+ ", DP URL: " + personDpUrl + ", DOB: " + personDOB + ", Gender: " + personGender);

				personDpUrl = personDpUrl.substring(0, personDpUrl.length() - 2) + DP_PIXEL_SIZE;

				intent = new Intent(this, ProfileDetailsActivity.class);

				// Passing profile details of user's through intent
				intent.putExtra(Constants.INTENT_KEY_PERSON_NAME, personName);
				intent.putExtra(Constants.INTENT_KEY_PERSON_DP_URL, personDpUrl);
				intent.putExtra(Constants.INTENT_KEY_PERSON_ABOUT_ME, personAboutMe);
				intent.putExtra(Constants.INTENT_KEY_PERSON_EMAIL, personEmail);
				intent.putExtra(Constants.INTENT_KEY_PERSON_DOB, personDOB);
				intent.putExtra(Constants.INTENT_KEY_PERSON_GENDER, personGender);

				startActivity(intent);
				this.finish();
			} else {
				intent = new Intent(this, ProfileDetailsActivity.class);
				startActivity(intent);
				this.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_screen1:
				signInGplus();
				break;
		}
	}

	/**
	 * SignIn using G+
	 */
	private void signInGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			isSignedIn = true;
			handleSignInError();
		}
	}
}
