package services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vidyanand on 15/8/15.
 */
public class ServiceResponse {

	public List<HashMap<String, String>> parseResponse(JSONObject jsonObject) {
		JSONArray jsonArray = null;
		try {
			jsonArray = jsonObject.getJSONArray("results");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getHospitals(jsonArray);
	}

	private List<HashMap<String, String>> getHospitals(JSONArray jsonArray) {
		int placesCount = jsonArray.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> placeMap = null;

		for (int i = 0; i < placesCount; i++) {
			try {
				placeMap = getHospital((JSONObject) jsonArray.get(i));
				placesList.add(placeMap);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return placesList;
	}

	private HashMap<String, String> getHospital(JSONObject googlePlaceJson) {
		HashMap<String, String> listHospital = new HashMap<String, String>();
		String hospitalName = "N/A";
		String vicinity = "N/A";
		String latitude = "";
		String longitude = "";
		String reference = "";

		try {
			if (!googlePlaceJson.isNull("name")) {
				hospitalName = googlePlaceJson.getString("name");
			}
			if (!googlePlaceJson.isNull("vicinity")) {
				vicinity = googlePlaceJson.getString("vicinity");
			}
			latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
			longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
			reference = googlePlaceJson.getString("reference");
			listHospital.put("place_name", hospitalName);
			listHospital.put("vicinity", vicinity);
			listHospital.put("lat", latitude);
			listHospital.put("lng", longitude);
			listHospital.put("reference", reference);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listHospital;
	}
}
