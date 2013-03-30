package com.mpcs.mytravelmemoirs;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewOldTripsActivity extends ListActivity {


	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_old_trips);


		Cursor TripsCursor = MainActivity.db.getAllTrips();
		startManagingCursor(TripsCursor);

		// Create an array to specify the fields we want to display in the list
		System.out.println("value of trip cursor" + TripsCursor);

		String[] from = new String[] { TripDbAdapter.trip_name };

		// and an array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { R.id.text1};

		// Now create a simple cursor adapter and set it to display
		//if(TripsCursor !=null)
		//{
		SimpleCursorAdapter trips = new SimpleCursorAdapter(this,R.layout.trips_row, TripsCursor, from, to);
		setListAdapter(trips);

	
		lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// selected item
				String trip = ((TextView) view).getText().toString();
				
				//System.out.println("\n\nSending auery to database : "+trip + "\n\n");
				
				int trip_id = MainActivity.db.gettripid(trip);

				/*Toast.makeText(getApplicationContext(), "trip_id: " + trip_id, Toast.LENGTH_LONG).show();*/
				// Launching new Activity on selecting single List Item
				Intent i = new Intent(getApplicationContext(), SingleTripActivity.class);
				// sending data to new activity

				i.putExtra("trip_id", trip_id);
				
				startActivity(i);

			}
		});

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_create_trip, menu);
		return true;
	}

}
