package com.acc.models;
/**
 * 
 * @author cemakpolat
 *
 */
public class Place {
	private int placeID;
	private String placeName;
	private int placeEnabled;
	public Place(){
		
	}
	
	public int getPlaceID() {
		return placeID;
	}
	public void setPlaceID(int placeID) {
		this.placeID = placeID;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	public int getPlaceEnabled() {
		return placeEnabled;
	}
	public void setPlaceEnabled(int i) {
		this.placeEnabled = i;
	}
}
