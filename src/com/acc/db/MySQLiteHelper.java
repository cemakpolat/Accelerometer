package com.acc.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.acc.models.Place;




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Database functionalities are performed in this class.
 * @author cemakpolat
 *
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "places.db";

	// Table Names
	private static final String TABLE_PLACE = "places";

	// Common column names
	private static final String KEY_ID = "id";

	// PLACE Table - column names
	private static final String KEY_PLACENAME = "placeName";
	private static final String KEY_PLACEENABLED = "placeEnabled";

	private static final String CREATE_TABLE_PLACE = "CREATE TABLE "
			+ TABLE_PLACE + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY autoincrement," + KEY_PLACENAME
			+ " TEXT," + KEY_PLACEENABLED + " BOOL" + ")";


	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// create tables
		database.execSQL(CREATE_TABLE_PLACE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE);

		// create new tables
		onCreate(db);
	}

	// ------------------------ "users" table methods ----------------//

	/*
	 * Creating a user
	 */

	public long insertPlace(Place Place) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PLACENAME, Place.getPlaceName());
		values.put(KEY_PLACEENABLED, Place.getPlaceEnabled());
		// insert row
		long Place_id = db.insert(TABLE_PLACE, null, values);

		return Place_id;

	}
	
	public int updateCagetory(Place place) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PLACENAME, place.getPlaceName());
		values.put(KEY_PLACEENABLED, place.getPlaceEnabled());
	//	System.out.println(place.getPlaceID()+" "+place.getPlaceEnabled());
		// updating row
		return db.update(TABLE_PLACE, values, KEY_ID + " = ?",new String[] { String.valueOf(place.getPlaceID()) });
	}
	
	public ArrayList<String> getOnlyEnabledPlaceNames(){
		ArrayList<String> PlaceList = new ArrayList<String>();

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT "+KEY_PLACENAME+" FROM " + TABLE_PLACE +" WHERE "+KEY_PLACEENABLED+"=1";

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				
				String PlaceName=c.getString(c.getColumnIndex(KEY_PLACENAME));
				// adding to todo list
				System.out.println(PlaceName);
				PlaceList.add(PlaceName);
			} while (c.moveToNext());
		}
		return PlaceList;
	}
	
	

	public void deletePlace(int PlaceID) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLACE, KEY_ID + " = ?",
				new String[] { String.valueOf(PlaceID) });
	}
	

	/**
	 * Check whether the given Place is already entered into the DB.
	 * @param PlaceName
	 * @return
	 */
	public boolean doesPlaceExist(String placeName){
		boolean result=false;
		ArrayList<Place> list=getAllPlaces();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getPlaceName().equalsIgnoreCase(placeName)){
				result=true;
				break;
			}
		}		
		return result;
		
	}
	
	/**
	 * Return all categories
	 * @return
	 */
	public ArrayList<Place> getAllPlaces() {
		ArrayList<Place> placeList = new ArrayList<Place>();

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_PLACE;

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Place cat = new Place();
				cat.setPlaceID(c.getInt(c.getColumnIndex(KEY_ID)));
				cat.setPlaceName((c.getString(c.getColumnIndex(KEY_PLACENAME))));
				cat.setPlaceEnabled(c.getInt(c.getColumnIndex(KEY_PLACEENABLED)));
				System.out.println("DB"+cat.getPlaceEnabled()+" "+cat.getPlaceName());
				// adding to todo list
				placeList.add(cat);
			} while (c.moveToNext());
		}
		return placeList;
	}
	/**
	 * Return all activated Categories
	 * @return
	 */
	public ArrayList<Place> getAllEnabledCategories() {
		ArrayList<Place> PlaceList = new ArrayList<Place>();

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_PLACE +" WHERE "+KEY_PLACEENABLED+"=1";

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Place cat = new Place();
				cat.setPlaceID(c.getInt(c.getColumnIndex(KEY_ID)));
				cat.setPlaceName((c.getString(c.getColumnIndex(KEY_PLACENAME))));
				cat.setPlaceEnabled(c.getInt(c.getColumnIndex(KEY_PLACEENABLED)));
				// adding to todo list
				PlaceList.add(cat);
			} while (c.moveToNext());
		}
		return PlaceList;
	}

	public int getPlaciesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PLACE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
//	public ArrayList<String> getPlacesNames(){
//	ArrayList<String> placeList = new ArrayList<String>();
//
//	SQLiteDatabase db = this.getReadableDatabase();
//	String selectQuery = "SELECT "+KEY_PLACENAME+" FROM " + TABLE_PLACE ;
//
//	Cursor c = db.rawQuery(selectQuery, null);
//
//	// looping through all rows and adding to list
//	if (c.moveToFirst()) {
//		do {
//			String PlaceName=c.getString(c.getColumnIndex(KEY_PLACENAME));
//			// adding to todo list
//			placeList.add(PlaceName);
//		} while (c.moveToNext());
//	}
//	return placeList;
//}
	/**
	 * get datetime
	 * */
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

}