package com.thb.vidyanand.assignmentthb;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import models.LocationDetailsModel;
import services.ServiceRequest;

/**
 * Created by vidyanand on 15/8/15.
 */
public class MapActivity extends AppCompatActivity {

	private static final String CLASS_TAG = MapActivity.class.getSimpleName();

	private ArrayList<LocationDetailsModel> mLocationDetailsModels;
	private GoogleMap mGoogleMap;
	private double mHospitalLat;
	private double mHospitalLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		initGoogleMap();
		getLocationDetails();
	}

	/**
	 * Initializing map
	 */
	private void initGoogleMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map)).getMap();

			// is map created successfully
			if (mGoogleMap == null) {
				Toast.makeText(getApplicationContext(), getString(R.string.unable_to_create_map), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initGoogleMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.menu_map_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_nearest_hospital:
				ServiceRequest serviceRequestHospitals = new ServiceRequest();
				Object[] toPass = new Object[2];
				toPass[0] = mGoogleMap;
				toPass[1] = getMapApiUrlForHospital(mHospitalLat, mHospitalLong);
				serviceRequestHospitals.execute(toPass);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Getting data from csv file located on server
	 */
	private ArrayList<LocationDetailsModel> getCSVFileDataFromUrl() {

		ArrayList<LocationDetailsModel> listLocationDetails = new ArrayList<>();

		try {
			URL textUrl = new URL("http://thehealthybillion.com/assignment/q4.csv");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(textUrl.openStream()));

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] RowData = line.split(",");

				Log.i(CLASS_TAG, "Location: "+RowData[0]+", Lattitude: "+RowData[1]+", Longitude: "+RowData[2]+", PointColor: "+RowData[3]);

				if(RowData[0].equalsIgnoreCase("Location A")) {
					mHospitalLat = Double.valueOf(RowData[1]);
					mHospitalLong = Double.valueOf(RowData[2]);
				}

				LocationDetailsModel locationDetailsModel = new LocationDetailsModel();

				locationDetailsModel.setLocationTile(RowData[0]);
				locationDetailsModel.setLattitude(RowData[1]);
				locationDetailsModel.setLongitude(RowData[2]);
				locationDetailsModel.setPointColor(RowData[3]);
				listLocationDetails.add(locationDetailsModel);
			}

			bufferedReader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listLocationDetails;
	}

	/**
	 * Giving call to getCSVFileDataFromUrl method from worker thread
	 * and fetching all the details of location from csv file
	 */
	private void getLocationDetails() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mLocationDetailsModels = getCSVFileDataFromUrl();

				for(int i=0; i< mLocationDetailsModels.size(); i++) {
					Log.i(CLASS_TAG, "Location: " + mLocationDetailsModels.get(i).getLocationTile() + ", Lattitude: "
							+ mLocationDetailsModels.get(i).getLattitude() + ", Longitude: " + mLocationDetailsModels.get(i).getLongitude()
							+ ", PointColor: " + mLocationDetailsModels.get(i).getPointColor());

					MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.valueOf(mLocationDetailsModels.get(i).getLattitude()),
							Double.valueOf(mLocationDetailsModels.get(i).getLongitude()))).title(mLocationDetailsModels.get(i).getLocationTile());

					if(mLocationDetailsModels.get(i).getPointColor().equalsIgnoreCase("Red")) {
						markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					} else if(mLocationDetailsModels.get(i).getPointColor().equalsIgnoreCase("Green")) {
						markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					} else if(mLocationDetailsModels.get(i).getPointColor().equalsIgnoreCase("Yellow")) {
						markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
					}

					Marker marker = mGoogleMap.addMarker(markerOption);
					marker.showInfoWindow();

					CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.valueOf(mLocationDetailsModels.get(i).getLattitude()),
							Double.valueOf(mLocationDetailsModels.get(i).getLongitude()))).zoom(12).build();

					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				}
			}
		}, 1000);
	}

	/**
	 * Creating map api url for hospital
	 * @param latitude
	 * @param longitude
	 * @return String of URL
	 */
	private String getMapApiUrlForHospital(double latitude, double longitude) {

		StringBuilder urlApiHospital = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");

		urlApiHospital.append("&location=");
		urlApiHospital.append(Double.toString(latitude));
		urlApiHospital.append(",");
		urlApiHospital.append(Double.toString(longitude));
		urlApiHospital.append("&radius=5000");
		urlApiHospital.append("&types=" + "hospital");
		urlApiHospital.append("&sensor=true&key=" + "AIzaSyCJtOH8DjFnY06FdA-venzNfq4pHM-qVks");

		Log.i(CLASS_TAG, "Hospital URL: " + urlApiHospital.toString());
		return urlApiHospital.toString();
	}
}
