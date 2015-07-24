package com.acc.clustering;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;

import com.acc.db.IOInterface;
import com.acc.db.MySQLiteHelper;
import com.acc.models.Point;


public class AccelerometerObserver {
	public static String TAG="com.acc.db.AccelerometerObserver";
	Hashtable<String, ArrayList<ArrayList<Point>>> dataSet =null;
	public MySQLiteHelper db;	
	IOInterface  io=new IOInterface();
	public static String FOLDER_ACCELEROMETER = "Accelerometer";
	public static String FILE_ACCELEROMETER="Accelerometer.txt";
	public static String FILE_ACCELEROMETER_JSON="JSON_Accelerometer.txt";
	private ArrayList<String> places=null;
	private ArrayList<Double> xlist=null;
	private ArrayList<Double> ylist=null;
	private ArrayList<Double> zlist=null;
	
	public AccelerometerObserver(){
	
	}
	public void prepareDataSets(){
		// read data set
		io.readAcceleromenterDataSet(FILE_ACCELEROMETER, FOLDER_ACCELEROMETER);
		// load data set
		this.dataSet=io.getDataSet();
		// get stored Places
		this.places=db.getOnlyEnabledPlaceNames();
		if(this.dataSet!=null && this.places!=null){
			xlist=new ArrayList<Double>();
			ylist=new ArrayList<Double>();
			zlist=new ArrayList<Double>();
			// add here what to do
			for(int i=0;i<this.dataSet.size();i++){
				ArrayList<ArrayList<Point>> list=dataSet.get(places.get(i));
				for(int j=0;j<list.size();j++){
					ArrayList<Point> pointList=list.get(j);
					for(int k=0;k<pointList.size();k++){
						Point p=pointList.get(k);
						Log.v(TAG," Points:"+ p.getX()+" "+p.getY()+" "+p.getZ());
						xlist.add(p.getX());
						ylist.add(p.getY());
						zlist.add(p.getZ());
					}
				}
			}
		}
	}
	 
	public void runObserver(){
		// run here the GMM algorithm
	}
}
