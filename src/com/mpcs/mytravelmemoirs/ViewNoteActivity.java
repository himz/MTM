package com.mpcs.mytravelmemoirs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNoteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

		

		
		setContentView(R.layout.activity_view_note);

		TextView txtTitle = (TextView) findViewById(R.id.textView1);
		TextView txtNote = (TextView) findViewById(R.id.textView2);
		
		Bundle extras = getIntent().getExtras();

		int noteId = 0;
		noteId = 	extras.getInt("noteId");
		String noteTitle = null;
		noteTitle = extras.getString("title");
		String note = null; 
		note = extras.getString("note");
		
		txtTitle.setText(noteTitle);
		txtNote.setText(note);
		
		Button btnBack = (Button) findViewById(R.id.button1);
		//EditText txtNote = (EditText) findViewById(R.id.body);
		
		
		btnBack.setOnClickListener(new View.OnClickListener() {


			private Context context = getApplicationContext();

			public void onClick(View v) {
				
				Intent i1 = new Intent(ViewNoteActivity.this, ViewOldTripsActivity.class);
				startActivity(i1);

			}
		});

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_note, menu);
		return true;
	}

}
