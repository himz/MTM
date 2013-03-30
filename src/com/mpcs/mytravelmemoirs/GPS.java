package com.mpcs.mytravelmemoirs;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GPS extends Service implements LocationListener {

	private final Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetNtwrkLocation = false;
	boolean canGetGPSLocation = false;

	private static Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	public GPS(Context context) {
		this.mContext = context;

		System.out.println("in USeGPS....gonna call getLocation()");
		getLocation();
		
		if(location == null)
		{
			
			System.out.println("TESTING111");
			Toast.makeText(mContext, "No working GPS found. Saving default city as Pittsburgh" +
					"", Toast.LENGTH_LONG).show();
			latitude = (int) (40.44 * 1E6);
			longitude = (int) (79.99 * 1E6);
			
			/*location.setLatitude(latitude);
			location.setLongitude(longitude);*/
		}

		System.out.println("^^^^^^^^^^^^^After getLocation()...the Location is "+ location);
	
	
		System.out.println("###### IsGPSEnabled is :" + canGetGPSLocation);
	}

	public Location getLocation() {
		try {

			System.out.println("In getLocation()");

			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  

			if(isGPSEnabled)
			{
				this.canGetGPSLocation = true;
				System.out.println("--------------------in IsGPSEnabled....GPS is :"+ canGetGPSLocation);
				if (location == null) {
					System.out.println("--------------------IF LOCATION IS NULL");
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("GPS Enabled", "GPS Enabled");
					if (locationManager != null) {

						System.out.println("--------------------Locationmgr!=null");
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {

							System.out.println("--------------------in location!=null");
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
						
					} 
				}
			}
			else if(isNetworkEnabled)
			{
				this.canGetNtwrkLocation = true;
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER,
						MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("Network", "Network");
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
					}
				}
			}
		} catch (Exception e) {
			Toast.makeText(mContext, "--------------------No GPS found. Saving default city as Pittsburgh" +
					"", Toast.LENGTH_LONG).show();
			System.out.println("Network and GPS not enabled!!");
			latitude = (int) (40.44 * 1E6);
			longitude = (int) (79.99 * 1E6);
		}

		System.out.println("--------------------In getLocation...returning Location as :"+location);
		return location;
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPS.this);
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetNtwrkLocation() {
		return this.canGetNtwrkLocation;
	}

	public boolean canGetGPSLocation() {
		return this.canGetGPSLocation;
	}

	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showNtwrkSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("Mobile Network settings");

		// Setting Dialog Message
		alertDialog.setMessage("Mobile Network is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public void showGPSSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public  String getCityName(double latitude, double longitude) {

		Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
		String cityName = null;
		List<Address> addresses = null;
		try {
			//if( gcd != null){
				addresses = gcd.getFromLocation(latitude, longitude, 1);
			/*} else if( gcd == null){
				int latitude1 = (int) (40.44 * 1E6);
				int longitude1 = (int) (79.99 * 1E6);
				addresses = gcd.getFromLocation(latitude1, longitude1, 1);
			}*/
				
				
				if ((addresses.size() != 0) && (addresses.size() > 0)) 
					cityName = addresses.get(0).getLocality();
				
				
		} catch (Exception e) {
			//Toast.makeText(getApplicationContext(), "Error in Geocoding", Toast.LENGTH_LONG).show();
			System.out.println("Error with location");
			
		}

		

		return cityName;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void setLatitude(double lat) {

		this.latitude = lat;
	}

	public void setLongitude(double lon) {

		this.longitude = lon;
	}

}
