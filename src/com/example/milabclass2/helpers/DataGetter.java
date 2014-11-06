package com.example.milabclass2.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.milabclass2.HomeScreen;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DataGetter extends AsyncTask<Void, Void, Boolean> {
	private ProgressDialog dialog;

	// url to update user status
	private final String url = "http://clashers.milab.idc.ac.il/php/milab_get_users.php";
	private List<NameValuePair> requestParams;
	private InputStream is = null;
	private String line = "";
	private String json = "";
	private JSONObject jObj = null;
	private HomeScreen parentActivity;

	public DataGetter(HomeScreen activity, List<NameValuePair> params) {
		parentActivity = activity;
		dialog = new ProgressDialog(parentActivity);
		this.requestParams = params;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Getting data from server, please wait.");
		dialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		boolean isSendOK = false;
		try {

			// create http request
			String paramString = URLEncodedUtils.format(requestParams, "utf-8");
			HttpGet httpGet = new HttpGet(url + "?" + paramString);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;

			// execute
			httpResponse = httpClient.execute(httpGet);

			// get response from server and parse it to json
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			line = reader.readLine();
			while (line != null) {
				sb.append(line + "\n");
				line = reader.readLine();
			}

			is.close();
			json = sb.toString();

			// try parse the string to a JSON object
			jObj = new JSONObject(json);

			// check json success tag
			int success = jObj.getInt("success");

			if (success == 1) {
				isSendOK = true;
				Log.d("MiLAB Class", "Data sender succeed: " + json.toString());
			} else {
				// failed to update product
				Log.d("MiLAB Class", "Data sender failed" + json.toString());
			}
		} catch (Exception e) {
			Log.d("MiLAB Class", "Data sender failed");
			e.printStackTrace();
		}

		return isSendOK;
	}

	@Override
	protected void onPostExecute(Boolean isSendOK) {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		
		if (isSendOK) {
			try {
				parentActivity.setDataFromServer(jObj.getJSONArray("users"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			CharSequence text = "Get Data Faild!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(parentActivity, text, duration);
			toast.show();
		}
	}

}
