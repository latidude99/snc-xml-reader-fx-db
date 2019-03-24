package com.latidude99.sncxmlreader.web;

public class Coordinates {
	public final double latitude;
	public final double longitude;
	
	public Coordinates(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
	}
	
	public Coordinates(String lat, String lng) {
		this.latitude = Double.parseDouble(lat.trim());
		this.longitude = Double.parseDouble(lng.trim());
	}

	@Override
	public String toString() {
		return "Coordinates [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
	
	

}
