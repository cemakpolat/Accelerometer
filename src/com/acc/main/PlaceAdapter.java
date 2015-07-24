package com.acc.main;

import java.util.ArrayList;

import com.acc.db.MySQLiteHelper;
import com.acc.models.Place;
import com.acc.main.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Place Adapter is implemented for the listview of the places which is called
 * in the AddPlaceActivity. In case the appearance of the add place listview
 * should be changed, this class ought to be modified.
 * 
 * @author cemakpolat
 * 
 */
public class PlaceAdapter  extends BaseAdapter {
    
	
    private Activity activity;
    private ArrayList<Place> places;
    private static LayoutInflater inflater=null;
    public MySQLiteHelper db;
   
    public PlaceAdapter(Activity a,  ArrayList<Place> places) {    	
    	activity = a;
    	db=new MySQLiteHelper(activity);
        this.places=places;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return places.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

   
    public void changeCategoryState(String placeName,int state){
    	for(int i=0;i<places.size();i++){
    		if(places.get(i).getPlaceName().equalsIgnoreCase(placeName)){
    			Place cat=new Place();
    			places.get(i);
    			cat.setPlaceID(places.get(i).getPlaceID());
    			cat.setPlaceName(places.get(i).getPlaceName());
    			cat.setPlaceEnabled(state);
    		//	Log.v("TAG",placeName +" "+state);
    			db.updateCagetory(cat);
    			break;
    		}
    	}    	
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.category_conf_row, null);

        //TextView text=(TextView)vi.findViewById(R.id.placeName);
        CheckBox chkIos=(CheckBox)vi.findViewById(R.id.chk_category_name);
        Button btnDelete=(Button)vi.findViewById(R.id.btn_delete);
        
        chkIos.setOnClickListener(new OnClickListener() {
      	  @Override
      	  public void onClick(View v) {

      		if (((CheckBox) v).isChecked()) {
      			changeCategoryState(((CheckBox) v).getText().toString(),1);
      			Toast.makeText(activity,"Place/Vehicle is enabled!)", Toast.LENGTH_LONG).show();
      		}else{
      			changeCategoryState(((CheckBox) v).getText().toString(),0);
      		}
       
      	  }
      	});
    
        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Toast.makeText(activity,"Category is checked!) "+places.get(position).getPlaceName(), Toast.LENGTH_LONG).show();
            	   AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	                 builder.setMessage("Do you want to remove " + places.get(position).getPlaceName() + "?");
	                 builder.setCancelable(false);
	                 builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								places.remove(places.get(position).getPlaceName());
								db.deletePlace(places.get(position).getPlaceID());
								notifyDataSetChanged();
								places=db.getAllPlaces();
							}
						});
				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.cancel();
							}
	                 });

	                 // Create and show the dialog
	                 builder.show();
            
            }
        });
        
        Place category=new Place();
        category=places.get(position);
        chkIos.setText(category.getPlaceName());
        if(category.getPlaceEnabled()==1){
        	chkIos.setChecked(true);	
        }else{
        	chkIos.setChecked(false);
        }
        //Log.v("TAG",category.getPlaceEnabled() +category.getPlaceName() +" ENABLED");
        return vi;
    }
}