package com.skplanet.trunk.carowner.MainPage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.skplanet.trunk.carowner.BuildConfig;
import com.skplanet.trunk.carowner.ChartActivity;
import com.skplanet.trunk.carowner.R;
import com.skplanet.trunk.carowner.TTSActivity;
import com.skplanet.trunk.carowner.common.CommonIntentService;
import com.skplanet.trunk.carowner.common.Constants;
import com.skplanet.trunk.carowner.common.LLog;
import com.skplanet.trunk.carowner.common.Volleyer;
import com.skplanet.trunk.carowner.model.BLERegion;
import com.skplanet.trunk.carowner.model.JsonObjectRequestTrunk;
import com.skplanet.trunk.carowner.model.SettingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getSimpleName();

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.fab)
	FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		initUI();
		initStetho();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// this에 속한 모든 Request를 Cancel
		Volleyer.getInstance(this).getRequestQueue().cancelAll(this);
	}

	@DebugLog
	private void initUI() {
		setSupportActionBar(toolbar);

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callToCallCenter(MainActivity.this);
			}
		});
	}

	public static final int REQUEST_CODE_CALL_PHONE = 2;

	private void callToCallCenter(Activity activity) {
		if (ActivityCompat.checkSelfPermission(activity,
				Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			// Request missing location permission.
			ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.CALL_PHONE },
					REQUEST_CODE_CALL_PHONE);
		} else {
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(activity.getString(R.string.call_center_number)));
			activity.startActivity(callIntent);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == REQUEST_CODE_CALL_PHONE) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// success!
				if (ActivityCompat.checkSelfPermission(this,
						Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					return;
				} else {
					Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string.call_center_number)));
					callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getApplicationContext().startActivity(callIntent);
				}
			} else {
				// Permission was denied or request was cancelled
				Toast.makeText(this, "Need Call phone permission", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void getInstanceIdToken() {
		if (checkPlayServices()) {
			// Start IntentService to register this application with GCM.
			Intent intent = new Intent(this, CommonIntentService.class);
			intent.setAction(Constants.ACTION_GCM_TOKEN_REFRESH);
			startService(intent);
		}
	}

	@DebugLog
	private void testVolley() {
		String URL_INIT = Constants.SERVER_HOST + "/v1/initialize";

		final JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("duid", "b5dcdfb4248b9618");
			jsonRequest.put("appId", "lIDZrVIKxHyLwPJc");
			jsonRequest.put("initType", "init");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequestTrunk(Request.Method.POST, URL_INIT, jsonRequest,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							LLog.d(TAG, "onResponse()..." + response);
							String requestToken = response.getString("requestToken");

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "onErrorResponse()... " + error.getLocalizedMessage());
					}
				});
		Volleyer.getInstance(this).addToRequestQueue(jsonObjectRequest);
	}

	@DebugLog
	private void testDB() {
		// DB write using ActiveAndroid
		BLERegion region = new BLERegion(1, 1, "abcd");
		region.save();
		// DB read
		List<BLERegion> regions = BLERegion.selectAll();
		for (BLERegion _region : regions) {
			LLog.d(TAG, "" + _region);
		}
	}

	@DebugLog
	private void testPreference() {
		// Preference write/read using ActiveAndroid
		SettingData settingData = SettingData.getSettingData();
		settingData.setValue(SettingData.FIELD_APP_FIRST_YN, "true");
		settingData.setValue(SettingData.FIELD_CHECKIN_FIRST_YN, "TEST");
		settingData.save();
		// reading
		boolean appFirstYn = settingData.getValueAsBoolean(SettingData.FIELD_APP_FIRST_YN);
		String checkin = settingData.getValue(SettingData.FIELD_CHECKIN_FIRST_YN);
		LLog.d(TAG, "[SharedPreference DB] appFirstYn: " + appFirstYn + " checkin: " + checkin);
	}

	@DebugLog
	private void initStetho() {
		// Debug모드에서만 Stetho를 initialize한다.
		if (BuildConfig.DEBUG) {
			try {
				Class clazz = Class.forName("com.facebook.stetho.Stetho");
				Class[] paramType = { Context.class };
				Method method = clazz.getDeclaredMethod("initializeWithDefaults", paramType);
				method.invoke(null, new Object[] { this });
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkPlayServices() {
		final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

		GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (googleApiAvailability.isUserResolvableError(resultCode)) {
				googleApiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@DebugLog
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.action_database:
			testDB();
			return true;
		case R.id.action_preference:
			testPreference();
			return true;
		case R.id.action_volley:
			testVolley();
			return true;
		case R.id.action_gcm:
			getInstanceIdToken();
			return true;
		case R.id.action_tts:
			startActivity(new Intent(this, TTSActivity.class));
			return true;
		case R.id.action_chart:
			startActivity(new Intent(this, ChartActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
