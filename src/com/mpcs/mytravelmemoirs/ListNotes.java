package com.mpcs.mytravelmemoirs;

import android.os.Bundle;

import android.app.ListActivity.*;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import android.app.ListActivity;
import android.widget.ListView;
import android.widget.Toast;




public class ListNotes extends ListActivity  {

	

	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_notes);
		
		Cursor notesCursor = MainActivity.db.getAllNotes();
		startManagingCursor(notesCursor);

		// Create an array to specify the fields we want to display in the list
		System.out.println("value of trip cursor" + notesCursor);

		String[] from = new String[] { TripDbAdapter.note_name };

		// and an array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { R.id.text1};

		// Now create a simple cursor adapter and set it to display
		//if(TripsCursor !=null)
		//{
		SimpleCursorAdapter trips = new SimpleCursorAdapter(this,R.layout.trips_row, notesCursor, from, to);
		setListAdapter(trips);

	
		lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// selected item
				String noteTitle = ((TextView) view).getText().toString();
				
				//System.out.println("\n\nSending auery to database : "+trip + "\n\n");
				
				int noteId = MainActivity.db.getNoteId(noteTitle);
				String note = MainActivity.db.getNote(noteTitle);

				/*Toast.makeText(getApplicationContext(), "trip_id: " + trip_id, Toast.LENGTH_LONG).show();*/
				// Launching new Activity on selecting single List Item
				Intent i = new Intent(getApplicationContext(), ViewNoteActivity.class);
				// sending data to new activity

				i.putExtra("noteId", noteId);
				i.putExtra("title", noteTitle);
				i.putExtra("note", note);
				
				startActivity(i);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_notes, menu);
		return true;
	}

}

