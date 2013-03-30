/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mpcs.mytravelmemoirs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class TripDbAdapter {

	public static final String trip_id = "_id";
	public static final String trip_name = "name";
	public static final String photo_id = "_id";
	public static final String photo_name = "name";
	public static final String photo_caption="caption";
	public static final String photo_city="city";
	public static final String photo_lat="lat";
	public static final String photo_lon="lon";
	public static final String photo_start="start";
	public static final String photo_stop="end";
	public static final String photo_inter="inter";
	public static final String photo_tid = "trip_id";
	public static final String note_name = "title";
	public static final String notes_id = "_id";



	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */



	private Context mCtx;


	//	private static String tripsCreate = "create table Trips (_id integer primary key autoincrement, name text not null, current boolean);";
	//	private static String tripDetailsCreate = "create table TripDetails(_id integer primary key autoincrement, startLocationId integer not null, endLocationId integer not null, startDate date not null, endDate date not null);";
	//	private static String locationsCreate = "create table Location(_id integer primary key autoincrement, name text not null);" ;
	//	private static String photosCreate = "create table Photos(_id integer primary key autoincrement, name text not null, caption text, trip_id integer, city text);";
	//	private static final String TAG = "DBHelper";
	//	private static final String DATABASE_NAME = "TripIt";
	//	private static final String DATABASE_TABLE1 = "Trips";
	//	private static final String DATABASE_TABLE2 = "TripDetails";
	//	private static final String DATABASE_TABLE3 = "Location";
	//	private static final String DATABASE_TABLE4 = "Photos";

	private static String tripsCreate = "create table Trips (_id integer primary key autoincrement, name text not null);";
	private static String tripDetailsCreate = "create table TripDetails(_id integer primary key autoincrement, startLocationId integer not null, endLocationId integer not null, startDate date not null, endDate date not null);";
	private static String locationsCreate = "create table Location(_id integer primary key autoincrement, trip_id integer , city text not null , lat double not null, lon double not null, start boolean , end boolean, inter boolean);" ;
	private static String photosCreate = "create table Photos(_id integer primary key autoincrement, name text not null, caption text, city text, trip_id integer);";
	private static String notesCreate = "create table Notes(_id integer primary key autoincrement, title text not null, city text, trip_id integer, note text);";
	
	private static final String TAG = "DBHelper";
	private static final String DATABASE_NAME = "TripIt";
	private static final String DATABASE_TABLE1 = "Trips";
	private static final String DATABASE_TABLE2 = "TripDetails";
	private static final String DATABASE_TABLE3 = "Location";
	private static final String DATABASE_TABLE4 = "Photos";
	private static final String DATABASE_TABLE5 = "Notes";

	private static final int DATABASE_VERSION = 2;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		private     Context context;

		DatabaseHelper(Context context) {


			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
			System.out.println("In DatabaseHelper constructor");

		}

		@Override
		public void onCreate(SQLiteDatabase db) {


			System.out.println("In onCreate()");

			db.execSQL(tripsCreate);
			db.execSQL(tripDetailsCreate);
			db.execSQL(locationsCreate);
			db.execSQL(photosCreate);
			db.execSQL(notesCreate);

			/*Toast.makeText(context, "4 table created", Toast.LENGTH_LONG).show();*/

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS TripIt");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx the Context within which to work
	 */
	public TripDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException if the database could be neither opened or created
	 */
	public TripDbAdapter open() throws SQLException {

		System.out.println("In open()");

		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createTrip(int id , String name)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(trip_id, id);
		initialValues.put(trip_name, name);
		//initialValues.put("current", true);
		return mDb.insert("Trips", null, initialValues);
	}

	public long endTrip(int id)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put("current", false);
		return mDb.update("Trips", initialValues, "current"+ "=" + "'true'" , null);
	}


	public boolean deleteTrip(int id)
	{
		return mDb.delete("Trips", id + "=" + id, null) > 0;

	}

	public long createNote(String title, String city, int trip_id, String note)
	{
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(note_name, title);
		initialValues.put("note", note);
		initialValues.put("city", city);
		initialValues.put("trip_id", trip_id);
		/*Toast.makeText(this.mCtx, "Created:" + name + caption, Toast.LENGTH_LONG).show();*/
		return mDb.insert("Notes", null, initialValues);
	}


	public long createPhoto(String name, String caption, String city, int trip_id)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(photo_caption, caption);
		initialValues.put(photo_name, name);
		initialValues.put("city", city);
		initialValues.put("trip_id", trip_id);
		/*Toast.makeText(this.mCtx, "Created:" + name + caption, Toast.LENGTH_LONG).show();*/
		return mDb.insert("Photos", null, initialValues);
	}


	public String getPhotoCaption(String name)
	{
		String caption = null;
		try
		{
			Cursor c = null;
			c = mDb.rawQuery("select caption from Photos where name=" + "\""+name+"\"", null);
			c.moveToFirst();
			caption = c.getString(c.getColumnIndex("caption"));
			c.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return caption; 
	}

	public long updateCaption(String name, String caption){
		ContentValues initialValues = new ContentValues();
		initialValues.put(photo_caption, caption);
		//initialValues.put(photo_name, name);
		/*Toast.makeText(this.mCtx, "TEST:" + name + caption, Toast.LENGTH_LONG).show();*/
		return mDb.update("Photos", initialValues, photo_name+ "='" + name +".jpg'" , null);
		//return mDb.insert("Photos", null, initialValues);

	}

	public void deletePhoto(int id)
	{

	}

	public Cursor getAllTrips()
	{
		System.out.println("Fetching all the trips");
		/*Toast.makeText(this.mCtx, "fetching the trips", Toast.LENGTH_LONG).show();*/


		return mDb.query("Trips", new String[] {trip_id, trip_name}, null, null, null, null, null);


	}

	
	public Cursor getAllNotes()
	{
		System.out.println("Fetching all the trips");
		/*Toast.makeText(this.mCtx, "fetching the trips", Toast.LENGTH_LONG).show();*/


		return mDb.query("Notes", new String[] {notes_id, note_name}, null, null, null, null, null);


	}


	public Cursor rawQuery_maxID(String query, Object object) {

		//	Toast.makeText(this.mCtx, "taking maximum id from DB", Toast.LENGTH_LONG).show();

		System.out.println("in rawquery_maxID function  ");

		return mDb.query("Trips",new String[] {trip_id}, null, null, null, null, null);
	}


	public Cursor rawQuery_inter(String query, Object object) {

		//	Toast.makeText(this.mCtx, "taking maximum id from DB", Toast.LENGTH_LONG).show();

		System.out.println("===========in rawquery_inter function===========  ");

		return mDb.query("Location",new String[] {photo_city, photo_lat,photo_lon , photo_start , photo_stop , photo_inter , photo_tid}, null, null, null, null, null);
	}


	public Cursor rawQuery_end(String query, Object object) {

		//	Toast.makeText(this.mCtx, "taking maximum id from DB", Toast.LENGTH_LONG).show();

		System.out.println("===========in rawquery_end function===========  ");

		return mDb.query("Location",new String[] {photo_city, photo_lat,photo_lon , photo_start , photo_stop , photo_inter , photo_tid}, null, null, null, null, null);
	}

	public long addLocation(int trip_id , String city , double lat, double lon, boolean start , boolean end, boolean inter)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put("end", end);
		initialValues.put("start", start);
		initialValues.put("city", city);
		initialValues.put("trip_id",trip_id);
		initialValues.put("lat", lat);
		initialValues.put("lon",lon);
		initialValues.put("inter",inter);

		/*Toast.makeText(this.mCtx, "ADded location:" + city + inter, Toast.LENGTH_LONG).show();*/


		return mDb.insert("Location", null, initialValues);

	}

	public int gettripid(String name)
	{
		int trip_id = 0;
		try
		{
			Cursor c = null;
			c = mDb.rawQuery("select _id from Trips where name =" + "\"" + name + "\"",null);
			c.moveToFirst();
			trip_id = c.getInt(c.getColumnIndex("_id"));	
			System.out.println("extracting the id:"+trip_id);
			c.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return trip_id;

	}

	
	public int getNoteId(String name)
	{
		int noteId = 0;
		try
		{
			Cursor c = null;
			c = mDb.rawQuery("select _id from Notes where title =" + "\"" + name + "\"",null);
			c.moveToFirst();
			noteId = c.getInt(c.getColumnIndex("_id"));	
			System.out.println("extracting the id:"+noteId);
			c.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return noteId;

	}
	
	
	public String getNote(String name)
	{
		String note = null;
		try
		{
			Cursor c = null;
			c = mDb.rawQuery("select note from Notes where title =" + "\"" + name + "\"",null);
			c.moveToFirst();
			note = c.getString(c.getColumnIndex("note"));	
			System.out.println("extracting note :"+ note);
			c.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return note;

	}
	
	

	


	public String getSC(int trip_id)
	{
		Cursor c = null;
		String startCity = new String();
		double lat = 0;
		double lon = 0;

		String query = "SELECT * FROM Location WHERE trip_id=" + "\"" + trip_id + "\" AND " +
				" start = 1";


		//String query = "SELECT city FROM Location WHERE start =" +  "\"" + 1 + "\"";

		c = MainActivity.db.rawQuery_inter(query, null);



		//c.moveToFirst();

		int id = 0;
		int photo_trip_id;
		if (c.moveToFirst())
		{
			do
			{           
				id = c.getInt(3); 
				photo_trip_id = c.getInt(6);
				if(id == 1 && (photo_trip_id == trip_id))
				{
					System.out.println("The start is : " + id);
					System.out.println("The trip_id is : " + photo_trip_id);

					startCity = c.getString(0);
					lat = c.getDouble(1);
					lon = c.getDouble(2);
				}

			} while(c.moveToNext());           
		}

		//startCity = c.getString(0);		

		System.out.println("size of cursor in getSC  = " + c.getCount());


		System.out.println("start city value in tripDBadapter is = " + startCity);
		System.out.println("start city latitude is = " + lat );

		System.out.println("start city longitude is = " + lon );
		return startCity;

	}



	public String getEC(int trip_id)
	{
		Cursor c = null;
		String endCity = new String();
		double lat = 0;
		double lon = 0;

		System.out.println("trip id in getEC is = " + trip_id);

		int photo_trip_id;

		/*	String query = "SELECT * FROM Location WHERE trip_id =" + "\"" + trip_id + "\" AND " +
				"end = "+ "\"" + true + "\"";*/

		String query = "SELECT * FROM Location WHERE trip_id=" + "\"" + trip_id + "\" AND " +
				" end = 1";

		c = MainActivity.db.rawQuery_end(query, null);

		//System.out.println("size of cursor in getEC  = " + c.getCount());

		int id = 0;
		if (c.moveToFirst())
		{
			do
			{           
				id = c.getInt(4); 
				photo_trip_id = c.getInt(6);

				if(id == 1 && (photo_trip_id == trip_id))
				{
					System.out.println("The end is : " + id);
					System.out.println("The trip_id is : " + photo_trip_id);

					endCity = c.getString(0);
					lat = c.getDouble(1);
					lon = c.getDouble(2);
				}

			} while(c.moveToNext());           
		}


		//	c.moveToFirst();

		//endCity = c.getString(0);		


	System.out.println("end city value in tripDBadapter is = " + endCity );
		System.out.println("end city latitude is = " + lat );

		System.out.println("end city longitude is = " + lon );

		return endCity;

	}

	public ArrayList<String> getIC(int trip_id)
	{
		Cursor c = null;
		ArrayList<String> interCity = new ArrayList<String>();
		double lat = 0;
		double lon = 0;
		int photo_trip_id;


		/*c = mDb.rawQuery("select * from Location where trip_id =" + "\"" + trip_id + "\" and " +
				"inter = "+ "\"" + true + "\""  ,null);*/

		String query = "SELECT * FROM Location WHERE trip_id=" + "\"" + trip_id + "\" AND " +
				" inter = 1";

		c = MainActivity.db.rawQuery_end(query, null);

		System.out.println("size of cursor in getIC  = " + c.getCount());

		int id = 0;
		if (c.moveToFirst())
		{
			do
			{           
				id = c.getInt(5); 
				photo_trip_id = c.getInt(6);

				if(id == 1 && (photo_trip_id == trip_id))
				{
					System.out.println("The end is : " + id);
					System.out.println("The trip_id is : " + photo_trip_id);
					interCity.add(c.getString(0));
					
				}

			} while(c.moveToNext());           
		}


		


		System.out.println("inter city value in tripDBadapter is = " + interCity);
		return interCity;

	}


	public double getLatitude(int trip_id, String city)
	{
		Cursor c = null;
		//String startCity = new String();
		double lat = 0;
		double lon = 0;
		int photo_trip_id;

		/*String query = "select * from Location where trip_id =" + "\"" + trip_id + "\" and " +
				"city = "+ "\"" + city + "\"" ;
		 */

		String query = "select * from Location where trip_id =" + "\"" + trip_id ;

		c = MainActivity.db.rawQuery_inter(query, null);

		System.out.println("size of cursor in getEC  = " + c.getCount());


		//c.moveToFirst();

		//int id = 0;
		String cityd = new String();
		if (c.moveToFirst())
		{
			do
			{           
				cityd = c.getString(0); 
				photo_trip_id = c.getInt(6);

				if(cityd.equals(city)  && (photo_trip_id == trip_id))
				{
					System.out.println("The city names  is : " + cityd);

					System.out.println("The trip_id is : " + photo_trip_id);
					lat = c.getDouble(1);

				}

			} while(c.moveToNext());           
		}


		//lat = c.getDouble(1);		


		System.out.println("value of lati for city is = " + lat + " and " + cityd);
		return lat;

	}


	public double getLongitude(int trip_id, String city)
	{
		Cursor c = null;
		//String startCity = new String();
		//double lat = 0;
		double lon = 0;
		int photo_trip_id;

		/*String query = "select * from Location where trip_id =" + "\"" + trip_id + "\" and " +
				"city = "+ "\"" + city + "\""  ;*/

		String query = "select * from Location where trip_id =" + "\"" + trip_id ;

		c = MainActivity.db.rawQuery_inter(query, null);


		System.out.println("size of cursor in getEC  = " + c.getCount());


		//c.moveToFirst();

		//int id = 0;
		String cityd = new String();
		if (c.moveToFirst())
		{
			do
			{           
				cityd = c.getString(0); 
				photo_trip_id = c.getInt(6);

				if(cityd.equals(city) && (photo_trip_id == trip_id))
				{
					System.out.println("The city names  is : " + cityd);

					System.out.println("The trip_id is : " + photo_trip_id);
					lon = c.getDouble(2);

				}

			} while(c.moveToNext());           
		}


		/*c.moveToFirst();

		lon = c.getDouble(2);		
		 */

		System.out.println("value of longitude for city is = " + lon + " and " + cityd);
		return lon;

	}



	public GPS getStartCity(int trip_id)
	{
		Cursor c = null;
		GPS startCity = new GPS(mCtx);
		double lat = 0;
		double lon = 0;

		c = mDb.rawQuery("select * from Location where _id =" + "\"" + trip_id + "\" and " +
				"start = "+ "\"" + true + "\""  ,null);
		c.moveToFirst();

		lat = c.getColumnIndex("lat");		
		lon = c.getColumnIndex("lon");


		startCity.setLatitude(lat);
		startCity.setLongitude(lon);

		return startCity;

	}

	public GPS getEndCity(int trip_id)
	{
		Cursor c = null;
		GPS endCity = new GPS(mCtx);
		double lat = 0;
		double lon = 0;

		c = mDb.rawQuery("select * from Location where _id =" + "\"" + trip_id + "\" and " +
				"end = "+ "\"" + true + "\""  ,null);
		c.moveToFirst();

		lat = c.getColumnIndex("lat");		
		lon = c.getColumnIndex("lon");

		endCity.setLatitude(lat);
		endCity.setLongitude(lon);

		return endCity;

	}



	public ArrayList<GPS> getIntermediateCities(int trip_id)
	{
		ArrayList<GPS> cityGeo = new ArrayList<GPS>();
		double lat = 0;
		double lon = 0;


		try
		{
			Cursor c = null;
			int i = 0;



			/*String query = "SELECT MAX(_id) AS max_id FROM Trips";*/

			String query = "select * from Location where _id =" + "\"" + trip_id + "\" and " +
					"end = "+ "\"" + true + "\"";


			//	c = mDb.rawQuery("select * from Location where inter = "+ "\"" + true + "\""  ,null);


			System.out.println("=========calling rawquery_intern function=====");

			c = MainActivity.db.rawQuery_inter(query, null);

			System.out.println("=========returning from rawquery_intern function=====");
			c.moveToFirst();


			System.out.println("NUMBER OF ENTRIES IN DB IS : " + c.getCount());

			while (c.moveToNext()) {


				GPS temp = null;

				temp = new GPS(mCtx);

				/*	lat = c.getColumnIndex("lat");		
					lon = c.getColumnIndex("lon");
				 */


				lat = c.getDouble(1);
				lon = c.getDouble(2);

				System.out.println("added intermediate city in tripdb , cityname = " + c.getString(0));

				System.out.println("value of lat is = " + lat);
				System.out.println("value of lon is = " + lon);



				temp.setLatitude(lat);
				temp.setLongitude(lon);

				System.out.println("ADDING INTERMEDIATE CITY 111 :...Num of times is " + i);

				cityGeo.add(temp);
				i++;

			}


			c.close();


		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return cityGeo;
	}

	public static ArrayList<String> convertStringToArray(String str)
	{
		List<String> items = new ArrayList<String>(Arrays.asList(str.split("\\s*,\\s*")));
		System.out.println("arraylist=" + items);
		return (ArrayList<String>) items;
	}


	public int getNumPhotosCity(String name, int trip_id)
	{
		int count =0 ;

		try
		{
			Cursor c = null;
			c = mDb.rawQuery("select * from Location where _id =" + "\"" + trip_id + "\"",null);
			c.moveToFirst();
			count = c.getCount();
			System.out.println("Number of photos:"+count);
			c.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return count;
	}

}