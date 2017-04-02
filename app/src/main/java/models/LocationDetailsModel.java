package models;

/**
 * Created by vidyanand on 15/8/15.
 */
public class LocationDetailsModel {

	private String mLocationTile;
	private String mLattitude;
	private String mLongitude;
	private String mPointColor;

	public String getLocationTile() {
		return mLocationTile;
	}

	public void setLocationTile(String mLocationTile) {
		this.mLocationTile = mLocationTile;
	}

	public String getLattitude() {
		return mLattitude;
	}

	public void setLattitude(String mLattitude) {
		this.mLattitude = mLattitude;
	}

	public String getLongitude() {
		return mLongitude;
	}

	public void setLongitude(String mLongitude) {
		this.mLongitude = mLongitude;
	}

	public String getPointColor() {
		return mPointColor;
	}

	public void setPointColor(String mPointColor) {
		this.mPointColor = mPointColor;
	}
}
