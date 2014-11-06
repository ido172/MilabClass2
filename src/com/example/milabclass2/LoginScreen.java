package com.example.milabclass2;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.milabclass2.helpers.DataSender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginScreen extends Activity {

	private EditText inputFirstName;
	private EditText inputLastName;
	private EditText inputCity;
	private int uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		
		//get input text objects
		inputFirstName = (EditText) findViewById(R.id.inputFirstName);
		inputLastName = (EditText) findViewById(R.id.inputLastName);
		inputCity = (EditText) findViewById(R.id.inputCity);
	}

	public void sendData(View v) {
		//send data from form to server
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("firstName", inputFirstName.getText().toString()));
		params.add(new BasicNameValuePair("lastName", inputLastName.getText().toString()));
		params.add(new BasicNameValuePair("city", inputCity.getText().toString()));
		new DataSender(this, params).execute();
	}
	
	public void saveUID(int uid) {
		if (uid != -1) {
			this.uid = uid;
			
			// save uid to data file
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt("uid", uid);
			editor.commit();

			//gotoHomeActivity();
		}
	}
	
	private int checkIfAlreadyLoggedIn() {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getInt("uid", -1);
	}
}
