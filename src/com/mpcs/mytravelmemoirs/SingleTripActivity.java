package com.mpcs.mytravelmemoirs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//
//import com.facebook.*;
//import com.facebook.model.*;
//import com.facebook.samples.hellofacebook.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SingleTripActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_trip);

		Button googleMaps = (Button) findViewById(R.id.maps);

		Button displaypics = (Button) findViewById(R.id.button2);

		Button uploadPics = (Button) findViewById(R.id.button1);

		Button displayNotes = (Button) findViewById(R.id.button3);


		displayNotes.setOnClickListener(new View.OnClickListener() {

			private Context context = getApplicationContext();

			public void onClick(View v) {

				Intent i3 = new Intent(SingleTripActivity.this, ListNotes.class);

				//				Toast.makeText(this.context, "Calling Google Maps", Toast.LENGTH_LONG).show();

				Bundle extras = getIntent().getExtras();

				int trip_id = extras.getInt("trip_id");

				i3.putExtra("trip_id", trip_id);

				startActivity(i3);


			}
		}); 


		
		
		googleMaps.setOnClickListener(new View.OnClickListener() {

			private Context context = getApplicationContext();

			public void onClick(View v) {

				Intent i3 = new Intent(SingleTripActivity.this, ViewGoogleMapsActivity.class);

				//				Toast.makeText(this.context, "Calling Google Maps", Toast.LENGTH_LONG).show();

				Bundle extras = getIntent().getExtras();

				int trip_id = extras.getInt("trip_id");

				i3.putExtra("trip_id", trip_id);

				startActivity(i3);


			}
		}); 



		uploadPics.setOnClickListener(new View.OnClickListener() {

			private Context context = getApplicationContext();

			public void onClick(View v) {

				
				String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyTravelMemoirs/" + MainActivity.currentTripId + "/" ;
				String dir1 ;
				System.out.println(dir);
				File folder = new File(dir);
				File folder1 ;
				File[] listOfFiles = folder.listFiles();
				File[] listOfFiles1; 
				String files = new String();
				String files1 = new String();
				List<String> fileNames = new ArrayList<String>();
				//List <Integer> ints = new ArrayList<Integer>();
				//IMG_20130223_110014.jpg
				
				//fileNames.add(dir);
				for (int i = 0; i < listOfFiles.length; i++) 
				{
					if (listOfFiles[i].isFile()) 
					{
						files = listOfFiles[i].getName();
						System.out.println("----------" + dir+files);
						fileNames.add(dir+ files);

					} else if(listOfFiles[i].isDirectory()) {
						dir1 = dir + listOfFiles[i].getName() + "/";
						folder1 = new File(dir1);
						listOfFiles1 = folder1.listFiles();
						/* Super Awesome faltu code */
						for (int j = 0; j < listOfFiles1.length; j++) 
						{
							if (listOfFiles1[j].isFile()) 
							{
								files1 = listOfFiles1[j].getName();
								System.out.println("----------" + dir1+files1);
								fileNames.add(dir1 + files1);

							} 
						}
					}
				}
				
				Context context = getApplicationContext();
				Toast.makeText(context, "Uploading Photos ..." , Toast.LENGTH_LONG).show();
				//Thread myrunnable = new Thread(new MyRunnable(fileNames),"T1"); //Thread create
				// Run as a daemon, so that even on page(app) exit, upload takes place				
				//myrunnable.setDaemon(true);
				//myrunnable.start();		

			}
/*
			private synchronized void uploadPhoto(String dir, File[] listOfFiles, int i) {
				String files;
				files = listOfFiles[i].getName();
				System.out.println(dir +files);
				Bitmap image = BitmapFactory.decodeFile(dir +files);
				
				System.out.println(image);
				
				
				Toast.makeText(this.context, "Uploading photo number : "+ i, Toast.LENGTH_LONG).show();	
				
				Request request = 
						Request.newUploadPhotoRequest(Session.getActiveSession(), image, new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						showPublishResult("Photo Post ", response.getGraphObject(), response.getError());
					}
				});
				request.executeAsync();
			}*/
		}); 


		displaypics.setOnClickListener(new View.OnClickListener() {


			private Context context = getApplicationContext();

			public void onClick(View v) {

				Intent i4 = new Intent(SingleTripActivity.this, ListCurrentTrip.class);

				//				Toast.makeText(this.context, "Calling Google Maps", Toast.LENGTH_LONG).show();

				Bundle extras = getIntent().getExtras();

				int trip_id = extras.getInt("trip_id");

				i4.putExtra("trip_id", trip_id);

				startActivity(i4);

			}
		}); 

	}


	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_single_trip, menu);
		return true;
	}
}


