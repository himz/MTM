package com.mpcs.mytravelmemoirs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class CaptureImage extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private static String fileName = null;
	private static String urlNote;
	double latitude;
	double longitude;
	private String appRoot;
	static String cityName = new String();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




		System.out.println("in captureImage #########");

		File maindir = new File(Environment.getExternalStorageDirectory()
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
		
		cityName = extras.getString("photoCity");

		}
		
		File citydir = new File(Environment.getExternalStorageDirectory()
                                + "/MyTravelMemoirs/"+ MainActivity.currentTripId +"/" + cityName + "/");
		if (!citydir.exists())
			citydir.mkdir();
		
		File imagedir = new File(Environment.getExternalStorageDirectory()
                + "/MyTravelMemoirs/"+ MainActivity.currentTripId +"/" + cityName + "/images/");
		if (!imagedir.exists())
			imagedir.mkdir();
		
		setContentView(R.layout.activity_capture_image);

		// create Intent to take a picture and return control to the calling
		// application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE , getApplicationContext()); // create a file to
		// save the image



		System.out.println("~~~~~~~~~~~~~~~~~~~~~" + MainActivity.NumPhotos);

		Log.d("MyCameraApp", "3: File created with filename" + fileUri);

		// set the image file name
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 





		// start the image capture Intent
		startActivityForResult(intent, 100);



		finish();
	}


	@SuppressLint("UseValueOf")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d("MyCameraApp", new Integer(requestCode).toString());
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				finish();

			} else if (resultCode == RESULT_CANCELED) {
				// User cancell
			} else {
				// Video capture failed, advise user
			}
		}
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type,Context context) {
		return Uri.fromFile(getOutputMediaFile(type,context));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type, Context context) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File imagedir = new File(Environment.getExternalStorageDirectory()
                                 + "/MyTravelMemoirs/"+ MainActivity.currentTripId +"/" + cityName + "/");

		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.
		Log.d("MyCameraApp", "File created with filename" + fileName);

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		File mediaFile;


		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(imagedir.getPath() + File.separator + "IMG_"
					+ timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(imagedir.getPath() + File.separator + "VID_"
					+ timeStamp + ".mp4");
		} else {
			return null;
		}
		Log.d("MyCameraApp", "File created with filename" + mediaFile);

		String name=mediaFile.toString();
		int index = name.lastIndexOf("/");
		name = name.substring(index + 1);		
		String caption="test caption";

		String city = new String("Bangalore");
		int trip_id = MainActivity.currentTripId;

		MainActivity.db.createPhoto(name,caption,city,trip_id);
		
		//String cityName = null;


		//Use GPS to get the start city name
		GPS gps = new GPS(context);
		double latitude = 0;
		double longitude = 0;


		latitude = gps.getLatitude();
		longitude = gps.getLongitude();

		cityName = gps.getCityName(latitude, longitude);

		//After taking photo, save the location in database table
		MainActivity.db.addLocation(MainActivity.currentTripId, cityName, latitude, longitude,  false, false, true);



		//		MediaScannerConnection.scanFile(context,
		//				new String[] { mediaFile.toString() }, null,
		//				new MediaScannerConnection.OnScanCompletedListener() {
		//			public void onScanCompleted(String path, Uri uri) {
		//				Log.i("ExternalStorage", "Scanned " + path + ":");
		//				Log.i("ExternalStorage", "-> uri=" + uri);
		//			}
		//		});

		System.out.println("55555555555");
		return mediaFile;
	}
}

