package com.mpcs.mytravelmemoirs;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;


public class ViewGoogleMapsActivity extends MapActivity
{

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private List<Overlay> mapOverlays;

	private Context mContext;
	private Drawable drawable;

	//For the list of all cities visited - used to draw lines through all visited cities
	private  List<GPS> intermediateCities = new ArrayList<GPS>();
	private GPS startCity = null;
	private GPS endCity = null;
	private GPS intermediateCity = null; 
	private int tripId = 0;
	
	private GeoPoint startCityGeo = null;
	private GeoPoint endCityGeo = null;

	
	private  List<GeoPoint> intermediateCitiesGeo = new ArrayList<GeoPoint>();



	class MapsOverlay  extends ItemizedOverlay
	{

		private String selectedCity;
		public MapsOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

			selectedCity = item.getTitle();

			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());

			dialog.setCancelable(true);

			dialog.setPositiveButton("View Photos", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent i = new Intent(ViewGoogleMapsActivity.this, ViewCityPhotosActivity.class);

					i.putExtra("selectedCity", selectedCity);
					i.putExtra("trip_id", tripId);


					/*Toast.makeText(mContext, "calling viewCityPhotos Activity)", Toast.LENGTH_LONG).show();*/

					startActivity(i);
				}
			});
			dialog.setNegativeButton("Close window", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});


			dialog.show();
			return true;
		}

		public void addOverlay(OverlayItem overlay) {
			mOverlays.add(overlay);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);

		}

		@Override
		public int size() {
			return mOverlays.size();

		}

		public void addOverlayItem(int lat, int lon, String title, String NumPhotos, Drawable altMarker) {
			GeoPoint point = new GeoPoint(lat, lon);
			OverlayItem overlayItem = new OverlayItem(point, title, NumPhotos);
			addOverlayItem(overlayItem, altMarker);
		}
		public void addOverlayItem(OverlayItem overlayItem, Drawable altMarker) {
			overlayItem.setMarker(boundCenterBottom(altMarker));
			addOverlayItem(overlayItem);
		}
		public void addOverlayItem(OverlayItem overlayItem) {
			mOverlays.add(overlayItem);
			populate();
		}



	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();

		int numPhotos = 0;

		Bundle extras = getIntent().getExtras();

		tripId = extras.getInt("trip_id");

		System.out.println("value of trip_Id is = " + tripId);

		/*if((tripId == 1))
		{
			System.out.println("in demo trip ");
			displayDemoValues();
			finish();
		}*/



		/*
		 * Before displaying the map overlays, get list of all cities
		 * visited on this trip, including start, end and intermediate cities 
		 */


		//START CITY
		drawable = this.getResources().getDrawable(R.drawable.start);
		MapsOverlay itemizedoverlay = new MapsOverlay(drawable, this);

		mapOverlays.add(itemizedoverlay);




		String startCityName = MainActivity.db.getSC(tripId);

		//startCity = MainActivity.db.getStartCity(tripId);
		numPhotos = MainActivity.db.getNumPhotosCity(null, tripId);

		/*
		double startCitylat = (startCity.getLatitude()) * 1E6;
		double startCitylng = (startCity.getLongitude()) * 1E6;
		String startCityName = startCity.getCityName(startCity.getLatitude(), startCity.getLongitude());

		System.out.println("startcity lat is :"+startCitylat);
		System.out.println("startcity long is :"+startCitylng);*/

		double startCitylat=MainActivity.db.getLatitude(tripId,startCityName);

		double startCitylng=MainActivity.db.getLongitude(tripId,startCityName);

		startCitylat = startCitylat * 1E6;
		startCitylng = startCitylng * 1E6;

		Drawable start = getResources().getDrawable(R.drawable.start);
		itemizedoverlay.addOverlayItem((int)startCitylat, (int)startCitylng, startCityName , "25 photos",  start);

		startCityGeo = new GeoPoint((int)startCitylat, (int)startCitylng);
		intermediateCitiesGeo.add(startCityGeo);



		//END CITY 
		drawable = this.getResources().getDrawable(R.drawable.end);


		String endCityName = MainActivity.db.getEC(tripId);

		//endCity = MainActivity.db.getEndCity(MainActivity.currentTripId);

		/*double endCitylat = (endCity.getLatitude()) * 1E6;
		double endCitylng = (endCity.getLongitude()) * 1E6;
		String endCityName = endCity.getCityName(endCity.getLatitude(), endCity.getLongitude());

		System.out.println("endcity lat is :"+endCitylat);
		System.out.println("endcity long is :"+endCitylng);*/

		double endCitylat=MainActivity.db.getLatitude(tripId,endCityName);
		double endCitylng=MainActivity.db.getLongitude(tripId,endCityName);

		endCitylat = endCitylat * 1E6;
		endCitylng = endCitylng* 1E6;
		
		Drawable end = getResources().getDrawable(R.drawable.end);
		itemizedoverlay.addOverlayItem((int)endCitylat, (int)endCitylng, endCityName, "55 photos",  end);

		endCityGeo = new GeoPoint((int)endCitylat, (int)endCitylng);


		//RANDOM INTERMEDIATE CITIES

		Drawable intermediate = getResources().getDrawable(R.drawable.ic_launcher);


		System.out.println("Calling getIntermediateCitites()");
		//intermediateCities =  MainActivity.db.getIntermediateCities(MainActivity.currentTripId);

		ArrayList<String> interCityName = new ArrayList<String>();

		interCityName = MainActivity.db.getIC(tripId);

		System.out.println("Size of intermediate cities is : "+ interCityName.size());
		String interCity = new String();

		for(int i = 0; i < interCityName.size(); i ++)
		{
			interCity = interCityName.get(i);

			/*double interCityLat = (intermediateCity.getLatitude() + i) * 1E6;
			double interCityLon = (intermediateCity.getLongitude() + i) * 1E6;
			String interCityName = intermediateCity.getCityName(intermediateCity.getLatitude(), intermediateCity.getLongitude());

			System.out.println("NAME OF INTERMED CITY IS : "+ interCityName);
			itemizedoverlay.addOverlayItem((int)interCityLat, (int)interCityLon, interCityName, "55 photos - inter",  intermediate);*/

			System.out.println("value of intercty from arraylist is = " + interCity);

			double interCitylat=MainActivity.db.getLatitude(tripId,interCity);
			double interCitylng=MainActivity.db.getLongitude(tripId,interCity);

			
			interCitylat = interCitylat * 1E6;
			interCitylng = interCitylng * 1E6;
			
			//Drawable end = getResources().getDrawable(R.drawable.end);
			itemizedoverlay.addOverlayItem((int)interCitylat, (int)interCitylng, interCity, "55 photos",  intermediate);
			intermediateCitiesGeo.add(new GeoPoint((int)interCitylat, (int)interCitylng));

		}



		/*
		itemizedoverlay.addOverlayItem(52372991, 4892655, "Amsterdam", "25 photos",  intermediate);
		itemizedoverlay.addOverlayItem(48857522, 2294496, "Paris", "33 photos",  intermediate);
		itemizedoverlay.addOverlayItem(51501851, -140623, "London", "44 photos", intermediate);

		 */

		//Drawing lines through all cities
		//Add all cities to geoPointsArray


		/*//startCity
		GPS startCity = new GeoPoint(19240000,-99120000);
		intermediateCities.add(startCity);



		//Iterate and add all intermediate cities
		GPS city1 = new GeoPoint(52372991, 4892655);
		intermediateCities.add(city1);

		GPS city2 = new GeoPoint(48857522, 2294496);
		intermediateCities.add(city2);

		GPS city3 = new GeoPoint(51501851, -140623);
		intermediateCities.add(city3);
		 */
		/*//endCity
		GeoPoint endCity = new GeoPoint(endCity.getLatitudeE6(), endCity.getLongitudeE6());
		intermediateCities.add(endCity);
		 */

              
		intermediateCitiesGeo.add(endCityGeo);

		
		LinearLayout maps = (LinearLayout) findViewById(R.id.maps);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();        
		Projection projection = mapView.getProjection();

		mapOverlays.add(new MyOverlay(projection, intermediateCitiesGeo, mapView));

		
		
		/*LinearLayout maps = (LinearLayout) findViewById(R.id.maps);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();        
		Projection projection = mapView.getProjection();

		
		
		mapOverlays.add(new MyOverlay(projection, intermediateCitiesGeo, mapView));

*/


		/*Toast.makeText(getApplicationContext(), "start city is : " +
				"", Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(), "end city is : " +
				"", Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(), "intermediate cities are : " +
				"", Toast.LENGTH_LONG).show();
*/



	}



	/*	private void displayDemoValues() {

		//Display dummy overlays for demo purpose, for 1 trips

		System.out.println("in displayDemovalues function ");

		//For World Trip
		if(tripId == 1)
		{

			//START CITY
			drawable = this.getResources().getDrawable(R.drawable.start);
			MapsOverlay itemizedoverlay = new MapsOverlay(drawable, this);

			mapOverlays.add(itemizedoverlay);



			Drawable start = getResources().getDrawable(R.drawable.start);

			int agraLat = (int) 27.1750f;
			int agraLon = (int) 78.0422f;

			agraLat = (int)(agraLat * 1E6);
			agraLon = (int)(agraLon * 1E6);
			itemizedoverlay.addOverlayItem(agraLat, agraLon, "agra" , "1 photos",  start);



			//END CITY 
			int endCitylat = (int) 12.9833f;
			int endCitylng = (int) 77.5833f;
			endCitylat = (int)(endCitylat * 1E6);
			endCitylng = (int)(endCitylng * 1E6);
			Drawable end = getResources().getDrawable(R.drawable.end);
			itemizedoverlay.addOverlayItem(endCitylat, endCitylng, "Bangalore", "55 photos",  end);


			drawable = this.getResources().getDrawable(R.drawable.end);

			endCity = MainActivity.db.getEndCity(MainActivity.currentTripId);

			double endCitylat = (MainActivity.db.ge) * 1E6;
			double endCitylng = (endCity.getLongitude()) * 1E6;
			String endCityName = endCity.getCityName(endCity.getLatitude(), endCity.getLongitude());

			System.out.println("endcity lat is :"+endCitylat);
			System.out.println("endcity long is :"+endCitylng);


			Drawable end = getResources().getDrawable(R.drawable.end);

			int nycLat = (int) 40.6893f;
			int nycLon = (int) 74.0446f;

			nycLat = (int)(nycLat * 1E6);
			nycLon = (int)(nycLon * 1E6);

			itemizedoverlay.addOverlayItem(nycLat, nycLon, "new york", "1 photos",  end);


			//RANDOM INTERMEDIATE CITIES
			Drawable android = getResources().getDrawable(R.drawable.ic_launcher);

			itemizedoverlay.addOverlayItem(52372991, 4892655, "amsterdam", "25 photos",  android);
			itemizedoverlay.addOverlayItem(48857522, 2294496, "paris", "33 photos",  android);
			itemizedoverlay.addOverlayItem(51501851, -140623, "london", "44 photos", android);



			//Drawing lines through all cities
			//Add all cities to geoPointsArray


			//startCity
			GeoPoint startCity = new GeoPoint(19240000,-99120000);
			geoPointsArray.add(startCity);



			//Iterate and add all intermediate cities
			GeoPoint city1 = new GeoPoint(52372991, 4892655);
			geoPointsArray.add(city1);

			GeoPoint city2 = new GeoPoint(48857522, 2294496);
			geoPointsArray.add(city2);

			GeoPoint city3 = new GeoPoint(51501851, -140623);
			geoPointsArray.add(city3);

			//endCity
			GeoPoint endCity = new GeoPoint(endCitylat, endCitylng);
			geoPointsArray.add(endCity);


			LinearLayout maps = (LinearLayout) findViewById(R.id.maps);
			MapView mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);

			mapOverlays = mapView.getOverlays();        
			Projection projection = mapView.getProjection();
		//	mapOverlays.add(new MyOverlay(projection, geoPointsArray, mapView));


		}
	}
	 */


	private String getCityName(GeoPoint point)
	{
		GPS gps = new GPS(mContext); 

		int latitude = point.getLatitudeE6();
		int longitude = point.getLongitudeE6();

		return (gps.getCityName(latitude, longitude));

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}





}


