package com.example.milabclass2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.milabclass2.helpers.DataGetter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class HomeScreen extends Activity {
	
	private ListView mainList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		mainList = (ListView)findViewById(R.id.mainContainer);
		Intent intent = getIntent();
		int uid = intent.getIntExtra("uid", -1);
		getDataFromServer(uid);
	}	
	
	
	public void getDataFromServer(int uid) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("uid", ""+uid));
		new DataGetter(this, params).execute();
	}
	
	public void setDataFromServer(JSONArray users) {
		try {

	        // create the grid item mapping
	        String[] from = new String[] {"uid", "First Name", "Last Name", "City", "Time"};
	        int[] to = new int[] { R.id.uid, R.id.firstName, R.id.lastName, R.id.city, R.id.time};
	 
	        // looping through All Users prepare the list of all records
	        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
	        for(int i = 0; i <  users.length(); i++){
	            HashMap<String, String> map = new HashMap<String, String>();
	            JSONObject row = users.getJSONObject(i);
	            map.put("uid", row.getString("uid"));
	            map.put("First Name", row.getString("first_name"));
	            map.put("Last Name", row.getString("last_name"));
	            map.put("City", row.getString("city"));
	            map.put("Time", row.getString("time"));
	            fillMaps.add(map);
	        }
	 
	        // fill in the grid_item layout
	        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.list_item, from, to);
	        mainList.setAdapter(adapter);
		} catch (Exception e) {
			
		}
	}
}
