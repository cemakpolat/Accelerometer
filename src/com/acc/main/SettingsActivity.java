package com.acc.main;


import com.acc.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * The sensitivity of the accelerometer is adjusted through this class.
 * Unfortunately, this is not persistent due to the fact that the sensitivity
 * value isn't saved. In other words, this value should be adjusted each time
 * when the application is opened. However, it can be easily stored either in a
 * file or in database.
 * 
 * @author cemakpolat
 * 
 */
public class SettingsActivity extends ActionBarActivity  implements OnSeekBarChangeListener{
	
	private float seekValue=0;
	private int maxSensitivityForSeekBar=50;
	private SeekBar bar; 
	private TextView textProgress,textAction,textSensitivity;
   
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        bar = (SeekBar)findViewById(R.id.seekBar1); // make seekbar object
        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
        
        
        // make text label for progress value
        textProgress = (TextView)findViewById(R.id.textViewProgress);
        textSensitivity = (TextView)findViewById(R.id.textSensitivity);
        
        // make text label for action
        textAction = (TextView)findViewById(R.id.textViewAction);
        bar.setProgress(Math.round(MainActivity.sensitivity ));
        bar.setMax(maxSensitivityForSeekBar);// set max
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Adjust Accelerometer Sensitivity");
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
    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
    	// TODO Auto-generated method stub
    	// change progress text label with current seekbar value
    	//textProgress.setText("The value is: "+progress);
    	// change action text label to changing
         seekValue=getConvertedValue(progress);
    }
    public float getConvertedValue(int intVal){
        float floatVal = 0;
        floatVal = .2f * intVal;
        return floatVal;
    }
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    	// TODO Auto-generated method stub
    	//textAction.setText("starting to track touch");
    	
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	// TODO Auto-generated method stub
    	seekBar.setSecondaryProgress(seekBar.getProgress());
    	MainActivity.sensitivity=seekValue;
    	textSensitivity.setText(""+seekValue );    	
    }
    
    public void saveSensitivityValue(){
    	
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