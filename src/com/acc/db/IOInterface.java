package com.acc.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import com.acc.models.Place;
import com.acc.models.Point;
import com.ditgsend.json.JSONArray;
import com.ditgsend.json.JSONException;
import com.ditgsend.json.JSONObject;

import android.os.Environment;
import android.util.Log;

/**
 * This class comprises the functionalities of the io which enables to write
 * into the file and read from the file. Additionally the collected
 * accelerometer can be gotten through this class.
 * 
 * @author cemakpolat
 * 
 */
public class IOInterface {
	public static String TAG="com.acc.db.IOInterface";
	private ArrayList<String> places=new ArrayList<String>();
	Hashtable<String, ArrayList<ArrayList<Point>>> globalDataSet = new Hashtable<String, ArrayList<ArrayList<Point>>>();
	
	public IOInterface(){
		
	}
	public void storeFile(String obj, String fileName,String folderName) {
			
		File file = new File(getRootPathFileDir(folderName), fileName);
			try {
				FileOutputStream out = new FileOutputStream(file, false);//FileOutputStream(file, true); for append
				out.write(obj.getBytes());
				out.flush();
				out.close();
	
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	/**
	 * Append the given string into the provided file
	 * @param outputString
	 * @param file
	 * @param folderName
	 */
	public void fileAppendBasic(String outputString,String file,String folderName) {
		try {

			File f;
			f = new File( getRootPathFileDir(folderName), file);

			FileOutputStream fos;
			// we are not appending... we are writing over...
			fos = new FileOutputStream(f, true);

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
			out.write(outputString + "\n");
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @param folderName
	 * @return the path of the provided folder
	 */
	public File getRootPathFileDir(String folderName){
		String root = Environment.getExternalStorageDirectory().toString();
    	File myDir = new File(root + "/"+folderName);    
    	boolean success = false;
    	if (!myDir.exists()) {
    	    success = myDir.mkdirs();
    	}

    	return myDir;
	}
	
	/**
	 * Read the given filename under the given folder name
	 * @param fileName
	 * @param folderName
	 * @return the content of the file
	 */
	public String readFileFromSDCard(String fileName,String folderName) {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File( root+"/"+folderName);
		File file = new File(myDir, fileName);
		if (!file.exists()) {
			//throw new RuntimeException("File not found");
			Log.e(TAG,"File not found");
			return "";
		}else{
			String str="";
			Log.e(TAG, "Starting to read");
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = reader.readLine()) != null) {
					str=str+line+"\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return str;
		}
	}
	
	
	/**
	 * Read the given filename under the given folder name
	 * @param fileName
	 * @param folderName
	 * @return the content of the file
	 * TODO: JSON Format
	 */
	
	public void readAcceleromenterDataSet(String fileName,String folderName) {
		
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File( root+"/"+folderName);
		File file = new File(myDir, fileName);
		
		ArrayList<Point> points=null;//new ArrayList<Point>();
		String previousPlace="";
		
		if (!file.exists()) {
			//throw new RuntimeException("File not found");
			Log.e(TAG,"File not found");
		}else{
			Log.e(TAG, "Starting to read");
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = reader.readLine()) != null) {
					Point p=null;
					if(isPlace(line)){
						if(points!=null && !previousPlace.equalsIgnoreCase("") && points.size()>0){
//							Log.v(TAG,"X :"+points.get(0).getX()+" prev place:"+previousPlace);
							if(globalDataSet.get(previousPlace)!=null){
								globalDataSet.get(previousPlace).add(points);	//get here the accurate vehicle type	
							}else{
								Log.v(TAG,"X :"+points.get(0).getX()+" prev place:"+previousPlace);
								ArrayList<ArrayList<Point>> list= new ArrayList<ArrayList<Point>>();
								list.add(points);
								globalDataSet.put(previousPlace, list);
							}
							
						}
						points=new ArrayList<Point>();
						previousPlace=line;									// set the new name
					}else{
//						Log.v(TAG,"Points:"+line);
						if(points!=null){
							String[] threePoints=line.split(",");
							p=new Point(Double.parseDouble(threePoints[0]),Double.parseDouble(threePoints[1]),Double.parseDouble(threePoints[2]));
							points.add(p);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	public Hashtable<String, ArrayList<ArrayList<Point>>> getDataSet(){
		if(this.globalDataSet.size()>0){
			return this.globalDataSet;
		}
		return null;
	}
	public JSONObject readAcceleromenterDataSetToJSON(String fileName,String folderName) {
		JSONObject obj=new JSONObject();
		for(int i=0;i<this.globalDataSet.size();i++){
			
			try {
				ArrayList<ArrayList<Point>> list=globalDataSet.get(places.get(i));
				JSONArray arrayList=new JSONArray();
				for(int j=0;j<list.size();j++){
					ArrayList<Point> pointList=list.get(j);
					JSONArray array=new JSONArray();
					for(int k=0;k<pointList.size();k++){
						
						Point p=pointList.get(k);
						JSONObject jsonItem=new JSONObject();
						//Log.v(TAG," JSON Points:"+ p.getX());
						jsonItem.put("x", p.getX());
						jsonItem.put("y", p.getY());
						jsonItem.put("z", p.getZ());
						array.put(jsonItem);
					}
					this.writeOut(array.toString());
					arrayList.put(array);
				}	
				this.writeOut(arrayList.toString());
				
				obj.put(places.get(i), arrayList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		storeFile(obj.toString(),fileName,folderName);
		//this.writeOut(obj.toString());
		return obj;
	}
	public void writeOut(String str){
		System.out.println(str);
	}
	/**
	 * Load the place list
	 * @param places
	 */
	public void loadPlaces(ArrayList<String> places){
		this.places=places;
	}
	public ArrayList<String> getPlaces(){
		return this.places;
	}
	/**
	 * Detect whether the place in the defined transports
	 * @param place
	 * @return 
	 */
	public boolean isPlace(String place){
		boolean state=false;
		for(String p:places){
			if(p.equalsIgnoreCase(place)){
				state=true;
				break;
			}
		}
		return state;
	}
    /**
     * This function does the same thing as readAcceleromenterDataSet, the unique difference, this function reads all file firstly, then put them 
     * in hash table. Indeed, the same thing is done two times. Therefore readAcceleromenterDataSet will be more efficient than this function. 
     * The goal was here to separate the reading function from here and use reading in a modular way. 
     * @param fileName
     * @param folderName
     */
	public void getAccelerometerDataSet(String fileName,String folderName){
    		String out= this.readFileFromSDCard(fileName,folderName);
    		ArrayList<Point> points=null;//new ArrayList<Point>();
    		String lines[] = out.split("\\n");
    		String previousPlace="";
    		for(int i=0;i<lines.length;i++){
    			Point p=null;
				if(isPlace(lines[i])){
					if(points!=null && !previousPlace.equalsIgnoreCase("")){
						//get here the accurate vehicle type
						globalDataSet.get(previousPlace).add(points);
					}
					points=new ArrayList<Point>();
					previousPlace=lines[i];// set the new name
				}else{
					if(points!=null){
						String[] threePoints=lines[i].split(",");
						p=new Point(Long.parseLong(threePoints[0]),Long.parseLong(threePoints[1]),Long.parseLong(threePoints[2]));
						points.add(p);
					}
				}
    	}
	}

}
