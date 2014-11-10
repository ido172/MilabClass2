package com.example.milabclass2;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.milabclass2.helpers.ServerAsyncParent;
import com.example.milabclass2.helpers.ServerCommunicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginScreen extends Activity implements ServerAsyncParent {

	private EditText inputFirstName;
	private EditText inputLastName;
	private EditText inputCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		
		inputFirstName = (EditText) findViewById(R.id.inputFirstName);
		inputLastName = (EditText) findViewById(R.id.inputLastName);
		inputCity = (EditText) findViewById(R.id.inputCity);
	}

	public void sendData(View v) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("firstName", inputFirstName.getText().toString()));
		params.add(new BasicNameValuePair("lastName", inputLastName.getText().toString()));
		params.add(new BasicNameValuePair("city", inputCity.getText().toString()));
		new ServerCommunicator(this, params, ServerCommunicator.METHOD_POST).execute("http://clashers.milab.idc.ac.il/php/milab_send_details.php");
	}
	
	public void goToHomeActivityAndFinish(int uid) {
		Intent intent = new Intent(this, HomeScreen.class);
		intent.putExtra("uid", uid);
		startActivity(intent);
		finish();
	}

	@Override
	public void doOnPostExecute(JSONObject jObj) {
		try {
			goToHomeActivityAndFinish(jObj.getInt("uid"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
