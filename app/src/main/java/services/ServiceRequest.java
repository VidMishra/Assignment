package services;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by vidyanand on 15/8/15.
 */
public class ServiceRequest extends AsyncTask {

	private String mNearestHospital;
	private GoogleMap mGoogleMap;

	@Override
	protected Object doInBackground(Object[] params) {
		try {
			mGoogleMap = (GoogleMap) params[0];
			String googleApiUrlHospital = (String) params[1];
			RequestTask requestTask = new RequestTask();
			mNearestHospital = requestTask.readPlaces(googleApiUrlHospital);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mNearestHospital;
	}

	@Override
	protected void onPostExecute(Object o) {
		ServiceParser serviceParser = new ServiceParser();
		Object[] toPass = new Object[2];
		toPass[0] = mGoogleMap;
		toPass[1] = o;
		serviceParser.execute(toPass);
	}
}
