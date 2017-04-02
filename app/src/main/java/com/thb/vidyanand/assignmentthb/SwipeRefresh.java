package com.thb.vidyanand.assignmentthb;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vidyanand on 14/8/15.
 */
public class SwipeRefresh extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

	private static final String CLASS_TAG = SwipeRefresh.class.getSimpleName();

	// Class members
	private ListView mLvData;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ArrayAdapter<String> mAdapter;
	private List<String> mListTextData;
	private TextView mTvSwipeMsg;
	private boolean isFileSize;
	private int mEnteryCounter;
	private int numberOfStringInFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_refresh);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mListTextData = new ArrayList<>();
		initViews();
	}

	/**
	 * Initializing views
	 */
	private void initViews() {
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_referesh_layout);
		mLvData = (ListView) findViewById(R.id.lv_screen_2);
		mTvSwipeMsg = (TextView) findViewById(R.id.tv_swipe_msg);
		mSwipeRefreshLayout.setOnRefreshListener(this);

		Button btnReset = (Button) findViewById(R.id.btn_reset);
		btnReset.setOnClickListener(this);
	}

	@Override
	public void onRefresh() {
		getRefreshContent();
	}

	/**
	 * On swipe refresh new data will being fetched from list
	 */
	private void getRefreshContent() {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mAdapter = new ArrayAdapter<String>(SwipeRefresh.this, R.layout.listview_row_textview, getTextListFromUrl());
				mTvSwipeMsg.setVisibility(View.GONE);
				mLvData.setAdapter(mAdapter);
				mSwipeRefreshLayout.setRefreshing(false);
				mEnteryCounter = 0;
			}
		}, 1000);
	}

	/**
	 * Getting string from the text file located on server
	 * @return List of String
	 */
	private List<String> getTextListFromUrl() {

		try {
			URL textUrl = new URL("http://thehealthybillion.com/assignment/q3.txt");
			BufferedReader brLength = new BufferedReader(new InputStreamReader(textUrl.openStream()));

			if(!isFileSize) {
				isFileSize = true;
				while (brLength.readLine() != null) {
					numberOfStringInFile++;
				}
			}
			brLength.close();

			BufferedReader brString = new BufferedReader(new InputStreamReader(textUrl.openStream()));
			String stringBuffer;

			while ((stringBuffer = brString.readLine()) != null) {
				Log.i(CLASS_TAG, stringBuffer);

				if(!mListTextData.contains(stringBuffer)) {
					mListTextData.add(stringBuffer);
					mEnteryCounter++;
				}

				if(mEnteryCounter == 3) {
					break;
				}
			}
			brString.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(numberOfStringInFile == mListTextData.size()){
			Toast.makeText(this, getString(R.string.msg_entery_up_to_date), Toast.LENGTH_SHORT).show();
		}

		Collections.sort(mListTextData);
		return  mListTextData;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_reset:
				mListTextData.clear();
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
				isFileSize = false;
				mEnteryCounter = 0;
				numberOfStringInFile = 0;
				mTvSwipeMsg.setVisibility(View.VISIBLE);
				break;
		}
	}
}
