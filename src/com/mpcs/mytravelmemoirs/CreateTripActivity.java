package com.mpcs.mytravelmemoirs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTripActivity extends Activity {

	//private TripDbAdapter db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);


		if (MainActivity.createchk)
		{
			setContentView(R.layout.activity_create_trip);
			Button btnStart = (Button) findViewById(R.id.captureImage);
			btnStart.setOnClickListener(myhandler);
			//		db = new TripDbAdapter(this);
			//		db.open();
			//		MainActivity.createchk = false;
		}
		else
		{
			Context context1 = getApplicationContext();
			Intent i2 = new Intent(CreateTripActivity.this, NewTripActivity.class);

			//	Toast.makeText(context1, "Calling current trip actiivty", Toast.LENGTH_LONG).show();

			startActivity(i2);
		}
	}


	View.OnClickListener myhandler = new View.OnClickListener() {

		public void onClick(View v) {


			Context context = getApplicationContext();
			EditText txtTripName = (EditText)findViewById(R.id.editText1);

			MainActivity.checkTrip=false;
			MainActivity.createchk = false;



			String tripname=txtTripName.getText().toString();

			//			Toast.makeText(context, "Current trip name is : " + tripname , Toast.LENGTH_LONG).show();


			MainActivity.currentTripId++;
			System.out.println("current rip id in create trip before:" + MainActivity.currentTripId);


			//Toast.makeText(context, "Vinay " + MainActivity.currentTripId , Toast.LENGTH_LONG).show();


			//Use GPS to get the start city name
			GPS gps = new GPS(context);
			String cityName = null;
			double latitude = 0;
			double longitude = 0;


			System.out.println("In CreateTripAcitivity.....111");

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			System.out.println("GGGGGGGGGGGG Lat, Lon in CreateTrip" + latitude + " " + longitude);
			
			if(latitude == 0 || longitude ==0){
				latitude = 40.4433f;
				longitude = 79.9436f;
				longitude = longitude * (-1);
			}
			System.out.println("Lat and long = "+latitude+" "+longitude);


			cityName = gps.getCityName(latitude, longitude);

			System.out.println("Start city name is : "+ cityName);

			//Create rows in tables for the Trip and the Location
			MainActivity.db.createTrip(MainActivity.currentTripId, tripname);
			MainActivity.db.addLocation(MainActivity.currentTripId, cityName, latitude, longitude,  true, false, false);

			System.out.println("current rip id in create trip after:" + MainActivity.currentTripId);


			Intent i = new Intent( CreateTripActivity.this , TripInfoActivity.class);
			i.putExtra("tripName", txtTripName.getText().toString());
			i.putExtra("startCity", cityName);

			startActivity(i);


		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_trip, menu);
		return true;
	}

}
