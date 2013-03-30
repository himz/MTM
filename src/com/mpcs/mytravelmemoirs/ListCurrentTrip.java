package com.mpcs.mytravelmemoirs;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListCurrentTrip extends ListActivity {
	//private Uri appRoot;
	//define source of MediaStore.Images.Media, internal or external storage
	//final Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	final Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	final Uri thumbUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
	final String thumb_DATA = MediaStore.Images.Thumbnails.DATA;
	final String thumb_IMAGE_ID = MediaStore.Images.Thumbnails.IMAGE_ID;

	final String uri = MediaStore.Images.Media.DATA;
	//final String condition = uri + " like '%/1/%'";


	//String myName;

	//SimpleCursorAdapter mySimpleCursorAdapter;
	MyAdapter mySimpleCursorAdapter;



	@Override
	public void onCreate(Bundle savedInstanceState) {


		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));








		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));





		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);


		//appRoot = Uri.parse(Environment.getExternalStorageDirectory() + "/MyTravelMemoirs/" + MainActivity.currentTripId+"/");

		Context context = getApplicationContext();

		//		Toast.makeText(context, "condition "+ condition, Toast.LENGTH_LONG).show();

		//sourceUri=appRoot;
		//		Toast.makeText(context, "sourceUri in list "+sourceUri, Toast.LENGTH_LONG).show();
		//thumbUri=appRoot;

		int trip_id;
		
		Bundle extras = getIntent().getExtras();
		if(extras==null)
		{
			trip_id=MainActivity.currentTripId;
		}
		else
		{
		
		trip_id = extras.getInt("trip_id");

		}

		String condition = uri + " like '%/" + trip_id+"/%'";

		String[] from = {MediaStore.MediaColumns.TITLE};
		int[] to = {android.R.id.text1};

		CursorLoader cursorLoader = new CursorLoader(
				this, 
				sourceUri, 
				null, 
				condition, 
				null, 
				MediaStore.Audio.Media.TITLE);

		Cursor cursor = cursorLoader.loadInBackground();

		mySimpleCursorAdapter = new MyAdapter(
				this, 
				android.R.layout.simple_list_item_1, 
				cursor, 
				from, 
				to, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		setListAdapter(mySimpleCursorAdapter);

		getListView().setOnItemClickListener(myOnItemClickListener);
	}



	//on item click listener

	OnItemClickListener myOnItemClickListener
	= new OnItemClickListener(){


		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cursor = mySimpleCursorAdapter.getCursor();
			cursor.moveToPosition(position);

			int int_ID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));

			String newName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));

			getThumbnail(int_ID,newName);
		}};

		//draw thumbnails

		private Bitmap getThumbnail(int id, final String newName){

			String[] thumbColumns = {thumb_DATA, thumb_IMAGE_ID};

			CursorLoader thumbCursorLoader = new CursorLoader(
					this, 
					thumbUri, 
					thumbColumns, 
					thumb_IMAGE_ID + "=" + id, 
					null, 
					null);


			//thumbCursorLoader.forceLoad();

			Cursor thumbCursor = thumbCursorLoader.loadInBackground();

			Bitmap thumbBitmap = null;
			if(thumbCursor.moveToFirst()){
				int thCulumnIndex = thumbCursor.getColumnIndex(thumb_DATA);

				String thumbPath = thumbCursor.getString(thCulumnIndex);

				/*Toast.makeText(getApplicationContext(), 
						newName, 
						Toast.LENGTH_LONG).show();*/

				thumbBitmap = BitmapFactory.decodeFile(thumbPath);

				//Create a Dialog to display the thumbnail
				AlertDialog.Builder thumbDialog = new AlertDialog.Builder(ListCurrentTrip.this);
				ImageView thumbView = new ImageView(ListCurrentTrip.this);

				thumbView.setImageBitmap(thumbBitmap);

				thumbView.setOnLongClickListener(new View.OnLongClickListener(){
					public boolean onLongClick(View v) {

						//Toast.makeText(getApplicationContext(),        "Start the activity here",Toast.LENGTH_LONG).show();

						AlertDialog.Builder builder = new AlertDialog.Builder(ListCurrentTrip.this);
						builder.setCancelable(true);
						builder.setTitle("Caption");
						builder.setInverseBackgroundForced(true);

						//TextView myMsg = new TextView(ListCurrentTrip.this);
						final EditText myMsg = new EditText(ListCurrentTrip.this);
						myMsg.setText(MainActivity.db.getPhotoCaption(newName + ".jpg"));
						myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
						builder.setView(myMsg);
						builder.setPositiveButton("Done",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String caption = myMsg.getText().toString();
								MainActivity.db.updateCaption(newName, caption);
								dialog.dismiss();
							}
						});
						builder.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
						AlertDialog alert = builder.create();
						alert.show();

						return true;

					}
				});





				LinearLayout layout = new LinearLayout(ListCurrentTrip.this);
				layout.setOrientation(LinearLayout.VERTICAL);
				layout.addView(thumbView);
				thumbDialog.setView(layout);
				thumbDialog.show();




			}else{
			/*	Toast.makeText(getApplicationContext(), 
						"NO Thumbnail!", 
						Toast.LENGTH_LONG).show();*/
			}

			return thumbBitmap;
		}

		public class MyAdapter extends SimpleCursorAdapter{

			Cursor myCursor;
			Context myContext;

			public MyAdapter(Context context, int layout, Cursor c, String[] from,
					int[] to, int flags) {
				super(context, layout, c, from, to, flags);

				myCursor = c;
				myContext = context;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row = convertView;
				if(row==null){
					LayoutInflater inflater=getLayoutInflater();
					row=inflater.inflate(R.layout.row, parent, false);	
				}

				ImageView thumbV = (ImageView)row.findViewById(R.id.thumb);
				TextView textV = (TextView)row.findViewById(R.id.text);

				myCursor.moveToPosition(position);

				int myID = myCursor.getInt(myCursor.getColumnIndex(MediaStore.Images.Media._ID));

				String myName = myCursor.getString(myCursor.getColumnIndex(MediaStore.Images.Media.TITLE));

				textV.setText(MainActivity.db.getPhotoCaption(myName + ".jpg"));

				String[] thumbColumns = {thumb_DATA, thumb_IMAGE_ID};
				CursorLoader thumbCursorLoader = new CursorLoader(
						myContext, 
						thumbUri, 
						thumbColumns, 
						thumb_IMAGE_ID + "=" + myID, 
						null, 
						null);

				Cursor thumbCursor = thumbCursorLoader.loadInBackground();

				Bitmap myBitmap = null;
				if(thumbCursor.moveToFirst()){
					int thCulumnIndex = thumbCursor.getColumnIndex(thumb_DATA);
					String thumbPath = thumbCursor.getString(thCulumnIndex);
					myBitmap = BitmapFactory.decodeFile(thumbPath);
					thumbV.setImageBitmap(myBitmap);
				}

				return row;
			}

		}


}
