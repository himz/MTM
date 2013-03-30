package com.mpcs.mytravelmemoirs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NewTripActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//		MainActivity.tripinfochk = false;


		setContentView(R.layout.activity_capture);



		Button btnCaptureImage = (Button) findViewById(R.id.captureImage);

		Button btnCurrentTrip = (Button) findViewById(R.id.btnCurrentTrip);	

		Button btnEndTrip = (Button) findViewById(R.id.btnEndTrip);
		
		Button btnCreateNote = (Button) findViewById(R.id.button2);

		//		Context context1 = getApplicationContext();
		//		Toast.makeText(context1, "Value of checkTrip in starting of end function = " + MainActivity.checkTrip, Toast.LENGTH_LONG).show();

		btnCreateNote.setOnClickListener(new View.OnClickListener() {

			private Context context = getApplicationContext();

			public void onClick(View v) {
				Intent i4 = new Intent(NewTripActivity.this, EditNote.class);

				Toast.makeText(context, "Calling CreateNoteActivity", Toast.LENGTH_LONG).show();
				
				String cityName = null;

				//Use GPS to get the start city name
//				GPS gps = new GPS(context);
				double latitude = 0;
				double longitude = 0;
//
//				latitude = gps.getLatitude();
//				longitude = gps.getLongitude();
//				
				if(latitude == 0 || longitude ==0){
					latitude = 40.4433f;
					longitude = 79.9436f;
					longitude = longitude * (-1);
				}
//				
//				cityName = gps.getCityName(latitude, longitude);
				cityName = "Pittsburgh";
				i4.putExtra("noteCity", cityName);

				startActivity(i4);
				MainActivity.db.addLocation(MainActivity.currentTripId, cityName, (int)latitude, (int)longitude,  false, false, true);
				//MainActivity.db.createNote(name, cityName, MainActivity.currentTripId);

			}
		});
		
		
		btnCurrentTrip.setOnClickListener(new View.OnClickListener() {

			private Context context = getApplicationContext();

			public void onClick(View v) {
				Intent i1 = new Intent(NewTripActivity.this, ListCurrentTrip.class);

				//				Toast.makeText(context, "Calling CurrentTripActivity", Toast.LENGTH_LONG).show();
				
								
				i1.putExtra("trip_id", MainActivity.currentTripId);

				startActivity(i1);
				
			}
		});


		btnCaptureImage.setOnClickListener(new View.OnClickListener() {

			private Context context = getApplicationContext();

			public void onClick(View v) {
               
                //Before starting CaptureImageActivity, use GPS to get current location
				String cityName = null;
                
				//Use GPS to get the start city name
				GPS gps = new GPS(context);
				double latitude = 0;
				double longitude = 0;
                
                
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
                
				if(latitude == 0 || longitude ==0){
					latitude = 40.4433f;
					longitude = 79.9436f;
					longitude = longitude * (-1);
				}
				
				cityName = gps.getCityName(latitude, longitude);
				
				Intent i = new Intent(NewTripActivity.this , CaptureImage.class);
				i.putExtra("photoCity", cityName);


			
				MainActivity.NumPhotos++;
				System.out.println("Added photo to DB...num photos is :" + MainActivity.NumPhotos);
				
				
				/*Toast.makeText(context, "WTF...num of photos is" +
						MainActivity.NumPhotos, Toast.LENGTH_LONG).show();*/

				
				startActivity(i);
                
                MainActivity.db.addLocation(MainActivity.currentTripId, cityName, (int)latitude, (int)longitude,  false, false, true);


			}
		});


		btnEndTrip.setOnClickListener(new View.OnClickListener() {

			private Context context = getApplicationContext();

			public void onClick(View v) {



				//Before ending trip, store end city location
				String cityName = null;

				//Use GPS to get the start city name
				GPS gps = new GPS(context);
				double latitude = 0;
				double longitude = 0;

				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				
				if(latitude == 0 || longitude ==0){
					latitude = 40.4433f;
					longitude = 79.9436f;
					longitude = longitude * (-1);
				}
				
				cityName = gps.getCityName(latitude, longitude);

				//save the location in database table
				MainActivity.db.addLocation(MainActivity.currentTripId, cityName, latitude, longitude,  false, true, false);

				/*Toast.makeText(context, "End city is" +
						cityName, Toast.LENGTH_LONG).show();*/


				Intent i2 = new Intent(NewTripActivity.this, MainActivity.class);

				startActivity(i2);
				//finish();
				MainActivity.db.close();
				MainActivity.tripinfochk = true;
				MainActivity.createchk = true;
				MainActivity.checkTrip = true;


			}
		});



	}

	/*	View.OnClickListener myhandler = new View.OnClickListener() {

		public void onClick(View v) {
			int id=1;

			Context context = getApplicationContext();

			//	EditText txtTripName = (EditText)findViewById(R.id.editText1);

			Intent i = new Intent( Capture.this , CaptureImage.class);

			//	i.putExtra("tripName", txtTripName.getText().toString());

			startActivity(i);


		}
	};*/



	@Override
	public void onBackPressed ()
	{
		if ( !MainActivity.tripinfochk)
		{

			Context context1 = getApplicationContext();
			//			Intent i2 = new Intent(TripInfoActivity.this, Capture.class);

			Toast.makeText(context1, "Click END TRIP.", Toast.LENGTH_LONG).show();

			//			startActivity(i2);

		}

		else
		{
			Intent i2 = new Intent(NewTripActivity.this, TripInfoActivity.class);

			//			Toast.makeText(context1, "Click END TRIP.", Toast.LENGTH_LONG).show();

			MainActivity.tripinfochk = true;
			startActivity(i2);
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_capture, menu);
		return true;
	}

}
