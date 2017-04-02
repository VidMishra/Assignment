package services;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vidyanand on 15/8/15.
 */
public class ServiceParser extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

	private JSONObject mJsonObject;
	private GoogleMap mGoogleMap;

	@Override
	protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

		List<HashMap<String, String>> listHospital = null;
		ServiceResponse serviceResponse = new ServiceResponse();

		try {
			mGoogleMap = (GoogleMap) inputObj[0];
			mJsonObject = new JSONObject((String) inputObj[1]);
			listHospital = serviceResponse.parseResponse(mJsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listHospital;
	}

	@Override
	protected void onPostExecute(List<HashMap<String, String>> list) {
		mGoogleMap.clear();

		for (int i = 0; i < list.size(); i++) {

			MarkerOptions markerOptions = new MarkerOptions();
			HashMap<String, String> listHospital = list.get(i);

			double lat = Double.parseDouble(listHospital.get("lat"));
			double lng = Double.parseDouble(listHospital.get("lng"));

			String placeName = listHospital.get("place_name");
			String vicinity = listHospital.get("vicinity");
			LatLng latLng = new LatLng(lat, lng);
			markerOptions.position(latLng);
			markerOptions.title(placeName + " : " + vicinity);

			Marker marker = mGoogleMap.addMarker(markerOptions);
			marker.showInfoWindow();

			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(12).build();
			mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}
}
