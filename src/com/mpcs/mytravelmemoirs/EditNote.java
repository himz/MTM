package com.mpcs.mytravelmemoirs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNote extends Activity {
	private Uri fileUri;
	static String cityName = new String();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
/*		File maindir = new File(Environment.getExternalStorageDirectory()
				+ "/MyTravelMemoirs/");
		if (!maindir.exists())
			maindir.mkdir();

		File subdir = new File(Environment.getExternalStorageDirectory()
				+ "/MyTravelMemoirs/"+ MainActivity.currentTripId +"/");
		if (!subdir.exists())
			subdir.mkdir();
		
		Bundle extras = getIntent().getExtras();
		if(extras==null)
		{
			cityName = "Pittsburgh";
		}
		else
		{
		
		cityName = extras.getString("noteCity");

		}

		
		File citydir = new File(Environment.getExternalStorageDirectory()
                                + "/MyTravelMemoirs/"+ MainActivity.currentTripId +"/" + cityName + "/");
		if (!citydir.exists())
			citydir.mkdir();
		
		File notesdir = new File(Environment.getExternalStorageDirectory()
                + "/MyTravelMemoirs/"+ MainActivity.currentTripId +"/" + cityName + "/notes/");
		if (!notesdir.exists())
			notesdir.mkdir();
		
		fileUri = getOutputMediaFileUri(getApplicationContext());*/
		cityName = "Pittsburgh";

		
		
		setContentView(R.layout.activity_edit_note);
		
		Button btnSave = (Button) findViewById(R.id.confirm);
		//EditText txtNote = (EditText) findViewById(R.id.body);
		
		btnSave.setOnClickListener(new View.OnClickListener() {
			EditText txtNote = (EditText) findViewById(R.id.body);
			EditText txtTitle = (EditText) findViewById(R.id.title);
			String title;
			int trip_id;
			String note;

			private Context context = getApplicationContext();

			public void onClick(View v) {
				note = txtNote.getText().toString();
				title = txtTitle.getText().toString();
				trip_id = MainActivity.currentTripId;
				MainActivity.db.createNote(title, cityName, trip_id, note);
				
				
				Intent i1 = new Intent(EditNote.this, NewTripActivity.class);
				Context context = getApplicationContext();
				Toast.makeText(context, "yes note inserted",Toast.LENGTH_LONG).show();
				startActivity(i1);

			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_note, menu);
		return true;
	}
	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(Context context) {
		return Uri.fromFile(getOutputMediaFile(context));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(Context context) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File notedir = new File(Environment.getExternalStorageDirectory()
                                 + "/MyTravelMemoirs/"+ MainActivity.currentTripId +"/" + cityName + "/notes/");

		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.
		//Log.d("MyCameraApp", "File created with filename" + fileName);

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		File mediaFile;
		mediaFile = new File(notedir.getPath() + File.separator + "NOTE_" + timeStamp);

		Log.d("MyCameraApp", "File created with filename" + mediaFile);

		String name=mediaFile.toString();
		int index = name.lastIndexOf("/");
		name = name.substring(index + 1);		
		

		//String city = new String("Bangalore");
		int trip_id = MainActivity.currentTripId;

				//Use GPS to get the start city name
		//MainActivity.db.createNote(name,cityName,trip_id);
		//After taking photo, save the location in database table
		//MainActivity.db.addLocation(MainActivity.currentTripId, cityName, latitude, longitude,  false, false, true);

		return mediaFile;
	}
}
	
