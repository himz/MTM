package com.mpcs.mytravelmemoirs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper	{

	private static String tripsCreate = "create table Trips ( id integer primary key autoincrement, name text not null);";
	private static String tripDetailsCreate = "create table TripDetails( id integer primary key autoincrement, startLocationId integer not null, endLocationId integer not null, startDate date not null, endDate date not null);";
	private static String locationsCreate = "create table Location( id integer primary key autoincrement, name text not null);" ;
	private static String photosCreate = "create table Photos( id integer primary key autoincrement, name text not null);";
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "TripIt";
    /*private static final String DATABASE_TABLE1 = "Trips";
    private static final String DATABASE_TABLE2 = "TripDetails";
    private static final String DATABASE_TABLE3 = "Location";
    private static final String DATABASE_TABLE4 = "Photos";*/
    
    private static final int DATABASE_VERSION = 2;

    //private final Context mCtx;

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL(tripsCreate);
		db.execSQL(tripDetailsCreate);
		db.execSQL(locationsCreate);
		db.execSQL(photosCreate);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
	}

}
