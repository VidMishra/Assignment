package com.thb.vidyanand.assignmentthb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;

/**
 * Created by vidyanand on 16/8/15.
 */
public class SplashScreenActivity extends Activity {

	private static final String CLASS_TAG = SplashScreenActivity.class.getSimpleName();

	// Class members
	private CountDownTimer mCountDownTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		final long WAITING_TIME = 1000 * 3;
		final long NOTIFY_TIME = 1000 * 3;

		mCountDownTimer = new CountDownTimer(WAITING_TIME, NOTIFY_TIME) {

			@Override
			public void onTick(long millisUntilFinished) {
				Log.i(CLASS_TAG, "SplashActivity:onTick");
			}

			@Override
			public void onFinish() {
				Context context = SplashScreenActivity.this.getApplicationContext();
				Intent intent;
				intent = new Intent(context, SignInActivity.class);
				startActivity(intent);
				finish();
			}
		};
		mCountDownTimer.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mCountDownTimer != null) {
			mCountDownTimer.cancel();
		}
	}
}
