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

public class TripInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//		MainActivity.createchk = false;

		if(MainActivity.tripinfochk)
		{
			setContentView(R.layout.activity_trip_info);
			
			
			EditText txtTripName = (EditText) findViewById(R.id.editText1);
			EditText txtStartCity = (EditText) findViewById(R.id.EditText02);
			
			
			String tripName = getIntent().getExtras().getString("tripName");
			String startCity = getIntent().getExtras().getString("startCity");
			
			txtTripName.setText(tripName);
			txtStartCity.setText(startCity);
			
			Button btnStart = (Button) findViewById(R.id.captureImage);
			btnStart.setOnClickListener(myhandler);
			//		MainActivity.tripinfochk = false;
		}
		else
		{
			Context context1 = getApplicationContext();
			Intent i2 = new Intent(TripInfoActivity.this, NewTripActivity.class);

			//	Toast.makeText(context1, "Calling current trip actiivty", Toast.LENGTH_LONG).show();

			startActivity(i2);
		}

	}

	@Override
	public void onBackPressed ()
	{
		if ( !MainActivity.createchk)
		{

			//			setContentView(R.layout.activity_create_trip);
			//			Button btnStart = (Button) findViewById(R.id.button1);
			//			btnStart.setOnClickListener(myhandler);
			//			//		db = new TripDbAdapter(this);
			//			//		db.open();
			//			MainActivity.createchk = false;

			Context context1 = getApplicationContext();
			Intent i2 = new Intent(TripInfoActivity.this, NewTripActivity.class);

			Toast.makeText(context1, "Your trip has been created. Click end trip to go back to previous screen.", Toast.LENGTH_LONG).show();

			startActivity(i2);

		}
		else
		{
			Context context1 = getApplicationContext();
			Intent i3 = new Intent(TripInfoActivity.this, CreateTripActivity.class);	
			MainActivity.createchk = true;
			startActivity(i3);
		}

	}

	View.OnClickListener myhandler = new View.OnClickListener() {

		public void onClick(View v) {
			int id=1;

			Context context = getApplicationContext();

			MainActivity.tripinfochk = false;


			//	EditText txtTripName = (EditText)findViewById(R.id.editText1);
			Intent i = new Intent( TripInfoActivity.this , NewTripActivity.class);
			//	i.putExtra("tripName", txtTripName.getText().toString());
			startActivity(i);


		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_trip_info, menu);
		return true;
	}

}
