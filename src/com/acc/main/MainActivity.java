package com.acc.main;

import com.acc.db.IOInterface;
import com.acc.db.MySQLiteHelper;
import com.acc.main.R;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * Accelerometer application aims to the collect accelerometer data in the according the places. A new place set can be prepared and accordingly 
 * the new data set can be collected. Briefly, the structure of this application is outlined below:
 * 
 * Main Activity: Starting and stopping the accelerometer as well as selecting the place where the accelerometer will be activated.
 * AccelerometerDirection: Service collects the accelerometer data and store them in the related file. 
 * AddPlaceAcitivity: This allow adding new place or updating/deleting/activating/deactivating the existing place. 
 * SettingActivity: Adjusting the accelerometer sensitivity, not persistent. This should be saved either in file or database
 * IOInterface: File Operations such as writing in file or reading from file. The all saved accelerometer data can be obtained through this class. 
 * Reading the data is implemented for the further use in order evaluate them later. The machine learning or AI algorithms can use directly these data. 
 * MySQLiteHelper: Database operations, updating/deleting/retrieving/creating the places.
 *  
 * @author cemakpolat
 *
 */
public class MainActivity extends ActionBarActivity implements OnClickListener{

	public static boolean serviceStarted=false;
	public static String vehicleType="Bus";
	public MySQLiteHelper db;		
	public ArrayList<String> placeList;
	public static float sensitivity=1;
	private Intent intent;
	public static int TOASTTIME=300;
	public static String FOLDER_ACCELEROMETER = "Accelerometer";
	public static String FILE_ACCELEROMETER="Accelerometer.txt";
	public static String FILE_ACCELEROMETER_JSON="JSON_Accelerometer.txt";
	public IOInterface io=null;
	 
	  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        findViewById(R.id.startBtn).setOnClickListener(this);
        findViewById(R.id.stopBtn).setOnClickListener(this);
        findViewById(R.id.placeBtn).setOnClickListener(this);
       
        db=new MySQLiteHelper(this);
        io=new IOInterface();
        //try this code!
        getAllPlaces();
      
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.startBtn:
        	if(!serviceStarted){
        		intent=new Intent(this,AccelerometerDirection.class);
        		intent.putExtra("vehicleType", vehicleType);
        		intent.putExtra("sensitivity", sensitivity);
        		startService(intent);
        		Toast.makeText(this,"Sensitivity: "+sensitivity,TOASTTIME).show();
        		serviceStarted=true;
        	}else{
        		Toast.makeText(this,"Service is already started!",TOASTTIME).show();
        	}
            break;
        case R.id.stopBtn:
        	if(serviceStarted){
        		intent=new Intent(this,AccelerometerDirection.class);
        		intent.putExtra("terminate", "activity terminated service");
            	startService(intent);
            	 serviceStarted=false;
            }else{
            	 Toast.makeText(this,"Service isn't started!",TOASTTIME).show();
            }
            break;
        case R.id.placeBtn:
        	showDialog();
        	break;
        }
    }
   
    ListView listViewDialog;
    private void showDialog(){
    	this.placeList=db.getOnlyEnabledPlaceNames();
    	

           AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
           LayoutInflater inflater = getLayoutInflater();
           View convertView = (View) inflater.inflate(R.layout.my_layout, null);
           alertDialog.setView(convertView);
          // alertDialog.setTitle("Select A Place");
           
           TextView title = new TextView(this);
	        // You Can Customise your Title here 
	        title.setText("Select A Place");
	        title.setBackgroundColor(Color.DKGRAY);
	        title.setPadding(10, 10, 10, 10);
	        title.setGravity(Gravity.CENTER);
	        title.setTextColor(Color.WHITE);
	        title.setTextSize(20);
	
	        alertDialog.setCustomTitle(title);
           
//           alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//               public void onClick(DialogInterface dialog, int id) {
//            	   dialog.cancel();
//               }
//           });
          alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
               }
           });
           ListView lv = (ListView) convertView.findViewById(R.id.list_enabled_places);
           DialogPlaceAdapter adapter = new DialogPlaceAdapter(MainActivity.this,placeList);
           lv.setAdapter(adapter);
           final Dialog dialog= alertDialog.create();
           dialog.show();
    	
           lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
      			Toast.makeText(MainActivity.this,"Selected Place/Vehicle: "+ placeList.get(position), Toast.LENGTH_SHORT).show();
      			vehicleType=placeList.get(position); // Assign the selected vehicle Type.
      			dialog.cancel();
      			
			}
		});
    }
    
    public String[] getAllPlaces(){
    	this.placeList=db.getOnlyEnabledPlaceNames();
    	io.loadPlaces(this.placeList); // loading the place list for iointerface 
    	String[] stockArr = new String[placeList.size()];
    	stockArr = placeList.toArray(stockArr);
    	return stockArr;
    }
    

    @Override
    protected void onResume() {
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();       
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//        case android.R.id.home:
//          // NavUtils.navigateUpFromSameTask(this);
//            return true;
        case R.id.menu_settings:
        	Intent intent1 = new Intent(this, SettingsActivity.class);
    	    startActivity(intent1);
             return true;
        
        case R.id.menu_save:
        	Intent intent = new Intent(this, AddPlaceActivity.class);
    	    startActivity(intent);
            return true;
        case R.id.menu_json_conversion:
        	  if(this.placeList.size()>0){
        		  io.readAcceleromenterDataSet(FILE_ACCELEROMETER, FOLDER_ACCELEROMETER);
        		  io.readAcceleromenterDataSetToJSON(FILE_ACCELEROMETER_JSON,FOLDER_ACCELEROMETER);
        		  Toast.makeText(MainActivity.this,"Accelerometer data is converted into JSON file which is located under "+FOLDER_ACCELEROMETER+" folder", Toast.LENGTH_LONG).show();
        	  }else{
        			Toast.makeText(MainActivity.this,"There is any defined Place for Accelerometer", Toast.LENGTH_SHORT).show();
        	  }
             return true;  
        }
        return super.onOptionsItemSelected(item);
    }
	
}

