package com.acc.main;

import java.util.ArrayList;

import com.acc.db.MySQLiteHelper;
import com.acc.models.Place;
import com.acc.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * In order to expand/shorten the place list adding a place was required. It is
 * quite easy to create a complete new Place set according to the case.
 * 
 * @author cemakpolat
 * 
 */
public class AddPlaceActivity extends ActionBarActivity {

	public MySQLiteHelper db;		
	EditText placeName;
	private ListView listView1;
	public static ArrayList<Place> placeList=new ArrayList<Place>();
	

	   public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.add_new_place);
	        ActionBar actionBar = getSupportActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);
	        actionBar.setTitle("Organize Places");
	    	db=new MySQLiteHelper(this);
			placeList=db.getAllPlaces();
			for(Place place: placeList){
				System.out.println(place.getPlaceEnabled()+" "+place.getPlaceID()+" "+place.getPlaceName());
			}
			
			listView1 = (ListView) findViewById(R.id.list_category);
			
			  // Click event for single list row
			listView1.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

				}
			});
			final PlaceAdapter adapter = new PlaceAdapter(this,placeList);
			listView1.setAdapter(adapter);
			Button addCategory = (Button) findViewById(R.id.btn_add_category);
			placeName = (EditText) findViewById(R.id.edit_category_name);
			addCategory.setOnClickListener(new OnClickListener() {

				// Run when button is clicked
				@Override
				public void onClick(View v) {

					//StringBuffer result = new StringBuffer();
					//Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();

					Place cat = new Place();
					cat.setPlaceName(placeName.getText().toString());
					db.insertPlace(cat);
					placeList = db.getAllPlaces();
					PlaceAdapter adapter = new PlaceAdapter(AddPlaceActivity.this,placeList);
					listView1.setAdapter(adapter);
					placeName.setText("");
				}
			});
	   }
		
	   @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			return true;
		}
		
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	        	onBackPressed();
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }	
}
