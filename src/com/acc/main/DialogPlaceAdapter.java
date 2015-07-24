package com.acc.main;

import java.util.ArrayList;

import com.acc.db.MySQLiteHelper;
import com.acc.main.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * DialogPlace adapter class is utilized for filling the dialog box while
 * selecting the place where Accelerometer should work. The data is provided by
 * the caller object.
 * 
 * @author cemakpolat
 * 
 */
public class DialogPlaceAdapter  extends BaseAdapter {
    
	
    private Activity activity;
    private ArrayList<String> places;
    private static LayoutInflater inflater=null;
    public MySQLiteHelper db;
   
    public DialogPlaceAdapter(Activity a,  ArrayList<String> places) {    	
    	activity = a;
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
   
   
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.dialog_row, null);

        TextView text=(TextView)vi.findViewById(R.id.text_place);
        //RadioButton chkIos=(RadioButton)vi.findViewById(R.id.radio_place);
        
       
//        chkIos.setOnClickListener(new OnClickListener() {
//      	  @Override
//      	  public void onClick(View v) {
//                      //is chkIos checked?
//      		if (((CheckBox) v).isChecked()) {
//      			//changeCategoryState(((CheckBox) v).getText().toString(),1);
//      			Toast.makeText(activity,"Category is checked or unchecked!)", Toast.LENGTH_LONG).show();
//      		}else{
//      			//changeCategoryState(((CheckBox) v).getText().toString(),0);
//      		}
//       
//      	  }
//      	});
    
        
        
        String category=places.get(position);
        text.setText(category);
        return vi;
    }
}