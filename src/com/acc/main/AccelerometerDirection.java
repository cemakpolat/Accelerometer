package com.acc.main;



import java.util.Random;

import com.acc.db.IOInterface;
import com.acc.main.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.FloatMath;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This class is responsible for saving the accelerometer data as a service. It
 * collects all data and store them in the related files.
 * 
 * @author cemakpolat
 * 
 */


public class AccelerometerDirection  extends Service implements SensorEventListener {
	
	public static String vehicleType="";
	public static String FOLDER_ACCELEROMETER = "Accelerometer";
	public static String FILE_ACCELEROMETER="accelerometer.txt";
	
    TextView textView;
    StringBuilder builder = new StringBuilder();
    IOInterface io=new IOInterface();
    
    float [] history = new float[2];
    String [] direction = {"NONE","NONE"};
     
    
 // Start with some variables
    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private float mAccel;
    public float sensitivityValue=1;
    private float mAccelCurrent;
    private float mAccelLast;
    public static int TOASTTIME=300;
    private int NOTIFICATION =423435;
    
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Accelerometer Service Created",TOASTTIME).show();
    }
    NotificationManager mNotificationManager;
    Notification notification;
    public void showNotification(){
    	  
        getApplicationContext();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Random generator = new Random();

        NotificationCompat.Builder builder =  
                new NotificationCompat.Builder(this)  
                .setSmallIcon(R.drawable.ic_launcher)  
                .setContentTitle("Accelerometer")
                .setContentText("Motions are being saved!")  
        		.setContentIntent(PendingIntent.getActivity(getApplicationContext(), generator.nextInt(), new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
  
  //      builder.setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        //long[] pattern = {500,500,500,500,500,500};
        long[] pattern = { 0, 100, 200, 300 };

        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        notification = builder.build();        
        //  startForeground( NOTIFICATION, notification );
         mNotificationManager.notify(NOTIFICATION, notification);
       
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String terminate = intent.getStringExtra("terminate");
        final String vehicleType = intent.getStringExtra("vehicleType");
        sensitivityValue = intent.getFloatExtra("sensitivity", sensitivityValue);
 
        if(vehicleType!=null){
        	if(!vehicleType.equalsIgnoreCase("")){
        		this.vehicleType=vehicleType;
        		io.fileAppendBasic(vehicleType,FILE_ACCELEROMETER,FOLDER_ACCELEROMETER);
        	}else{
        		Toast.makeText(this,"Accelerometer is started for the vehicle "+vehicleType+"",TOASTTIME).show();
        	}
        }
        
        if(terminate != null) {
            Toast.makeText(this, " Accelerometer stopped", TOASTTIME).show();
            sensorMan.unregisterListener(this);
            stopSelf();
        }else{
        	  sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
              accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
              mAccel = 0.00f;
              mAccelCurrent = SensorManager.GRAVITY_EARTH;
              mAccelLast = SensorManager.GRAVITY_EARTH;
              sensorMan.registerListener(this, accelerometer,
                      SensorManager.SENSOR_DELAY_UI);
              showNotification();
        	Toast.makeText(this,"Accelerometer is started for the vehicle "+vehicleType +"",TOASTTIME).show();
        }
        return START_STICKY;
    }
    
    //SECOND
    @Override
    public void onSensorChanged(SensorEvent event) {
	    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
	        mGravity = event.values.clone();

	        // Shake detection
	        float x = mGravity[0];
	        float y = mGravity[1];
	        float z = mGravity[2];
	        mAccelLast = mAccelCurrent;
	       
	        mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
	        float delta = mAccelCurrent - mAccelLast;
	        mAccel = mAccel * 0.9f + delta;
	        if(mAccel > sensitivityValue){ 
	        	String str=x+","+y+","+z;
	        	io.fileAppendBasic(str,FILE_ACCELEROMETER,FOLDER_ACCELEROMETER);
	        }
	    }
	    io.readAcceleromenterDataSet(FILE_ACCELEROMETER, FOLDER_ACCELEROMETER);
    }
    @Override
    public void onDestroy() 
    {
        super.onDestroy();
       mNotificationManager.cancel(NOTIFICATION);
        //Toast.makeText(this, "My Service Stopped", Toast.LENGTH_SHORT).show();
    }

    //FIRST
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}