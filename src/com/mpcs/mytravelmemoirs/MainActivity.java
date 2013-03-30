package com.mpcs.mytravelmemoirs;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String appRoot;

	public static int currentTripId=0 ;
	public static int NumPhotos = 0;

	private List<String> item = null;
	private List<String> path = null;

	public static  boolean checkTrip = true;
	public static boolean createchk = true;
	public static boolean tripinfochk = true;

	public static TripDbAdapter db;
	public static boolean staticdata = true;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

				//Context context = getApplicationContext();
				//Toast.makeText(context, "Value of checkTrip = " + checkTrip, Toast.LENGTH_LONG).show();


		if(checkTrip)
		{



			setContentView(R.layout.activity_main);
			Button btnNewTrip = (Button) findViewById(R.id.btnNewTrip);

			Button btnOldTrips = (Button) findViewById(R.id.btnOldTrips);		

			Button btnCurrentTrip = (Button) findViewById(R.id.btnCurrentTrip);	
			//Button ToDo = (Button) findViewById(R.id.ToDo);	
			Button help = (Button) findViewById(R.id.Help);	


			db = new TripDbAdapter(this);
			db.open();
			if(staticdata)
			{
				
				
				currentTripId++;

				double agraLat =  27.1750f;
				double agraLon =  78.0422f;

				/*agraLat = (int)(agraLat * 1E6);
				agraLon = (int)(agraLon * 1E6);*/

				db.addLocation(MainActivity.currentTripId, "agra", agraLat, agraLon,  true, false, false);

				double parisLat =  48.8584f;
				double parisLon =  2.2946f;
				
				db.addLocation(MainActivity.currentTripId, "paris", parisLat, parisLon,  false, false, true);

				double amsterdamLat =  52.3700f;
				double amsterdamLon =  4.8900f;

				db.addLocation(MainActivity.currentTripId, "amsterdam", amsterdamLat, amsterdamLon,  false, false, true);

				double londonLat =  51.5171f;
				double londonLon =  0.1062f;
				londonLon = londonLon * (-1);
				
				db.addLocation(MainActivity.currentTripId, "london", londonLat, londonLon,  false, false, true);

				double nycLat =  40.6893f;
				double nycLon =  74.0446f;

				/*nycLat = (int)(nycLat * 1E6);
				nycLon = (int)(nycLon * 1E6);*/
				nycLon = nycLon * (-1);

				db.addLocation(MainActivity.currentTripId, "new york", nycLat, nycLon, false, true, false);


				db.createTrip(MainActivity.currentTripId, "WorldTrip");

				MainActivity.db.createPhoto("agra.jpg","TAJ MAHAL","agra",1);
				MainActivity.db.createPhoto("paris.jpg","EIFEL TOWER","paris",1);
				MainActivity.db.createPhoto("amsterdam.jpg","AMSTERDAM","amsterdam",1);
				MainActivity.db.createPhoto("london.jpg","LONDON","london",1);
				MainActivity.db.createPhoto("new york.jpg","STATUE OF LIBERTY","new york",1);

				
				System.out.println("value of currenttripid = " + currentTripId);

				staticdata = false;
				
			}

			currentTripId=getMaxId();

			System.out.println("Current trip id in create is :"+ currentTripId);

			btnNewTrip.setOnClickListener(new View.OnClickListener() {

				private Context context = getApplicationContext();

				public void onClick(View v) {

					Intent i1 = new Intent(MainActivity.this, CreateTripActivity.class);

					//			Toast.makeText(this.context, "Calling CreateTripActivity", Toast.LENGTH_LONG).show();

					//					checkTrip = true;
					startActivity(i1);

				}
			});



			btnOldTrips.setOnClickListener(new View.OnClickListener() {

				private Context context = getApplicationContext();

				public void onClick(View v) {

					Intent i2 = new Intent(MainActivity.this, ViewOldTripsActivity.class);

					//Toast.makeText(this.context, "Calling ViewOldTripsActivity", Toast.LENGTH_LONG).show();

					startActivity(i2);

				}
			}); 
	
/*			ToDo.setOnClickListener(new View.OnClickListener() {

				private Context context = getApplicationContext();

				public void onClick(View v) {

					Intent i2 = new Intent(MainActivity.this, HelpActivity.class);

					//Toast.makeText(this.context, "Calling ViewOldTripsActivity", Toast.LENGTH_LONG).show();

					startActivity(i2);

				}
			}); */
			
			help.setOnClickListener(new View.OnClickListener() {

				private Context context = getApplicationContext();

				public void onClick(View v) {

					Intent i2 = new Intent(MainActivity.this, HelpActivity.class);

					//Toast.makeText(this.context, "Calling ViewOldTripsActivity", Toast.LENGTH_LONG).show();

					startActivity(i2);

				}
			}); 
	
	
			
			
			
			

		}

		else
		{
			Context context1 = getApplicationContext();
			Intent i2 = new Intent(MainActivity.this, NewTripActivity.class);

			//	Toast.makeText(context1, "Calling current trip actiivty", Toast.LENGTH_LONG).show();

			startActivity(i2);

		}
	}


	private int getMaxId()
	{
		String query = "SELECT MAX(_id) AS max_id FROM Trips";
		Cursor cursor = db.rawQuery_maxID(query, null);

		int id = 0;     
		if (cursor.moveToFirst())
		{
			do
			{           
				id = cursor.getInt(0);   
				System.out.println("The id is : " + id);
			} while(cursor.moveToNext());           
		}
		return id;
	}



	@Override
	public void onBackPressed ()
	{
		if (checkTrip)
		{

			System.out.println("value of chekctrip is " + checkTrip);
			
			Intent intent = new Intent(Intent.ACTION_MAIN);
		    intent.addCategory(Intent.CATEGORY_HOME);
		    startActivity(intent);
			
		}


	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
