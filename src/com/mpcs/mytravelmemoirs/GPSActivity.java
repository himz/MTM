package com.mpcs.mytravelmemoirs;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class GPSActivity extends Activity {

	Button btnNtwrkLoc;
	Button btnGPSLoc;

	// GPSTracker class
	GPS gps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		gps = new GPS(GPSActivity.this);	

		// check if GPS enabled
		if(gps.canGetNtwrkLocation())
		{

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			// \n is for new line
			Toast.makeText(getApplicationContext(), "Your Mobile Network Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		
			String cityName = getCityName(latitude, longitude);
					
					
			//Add lat, lon, city name to Photo DB
			
		}
		else
		{
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showNtwrkSettingsAlert();
		}

	}

	private String getCityName(double latitude, double longitude) {
		Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Error in Geocoding", Toast.LENGTH_LONG).show();
		}
		
		String cityName = null;
		if ((addresses.size() != 0) && (addresses.size() > 0)) 
			cityName = addresses.get(0).getLocality();
		
		return cityName;
	}
}

