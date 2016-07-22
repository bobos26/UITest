package com.skplanet.prototype.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.skplanet.prototype.R;
import com.skplanet.prototype.gcm.RegistrationIntentService;

/**
 * Created by 1001955 on 3/17/16.
 */
public class GCMActivity extends Activity {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private BroadcastReceiver mRegistrationBroadcastReceiver;
	private ProgressBar mRegistrationProgressBar;
	private TextView mInformationTextView;
	private boolean isReceiverRegistered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gcm);

		mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
				SharedPreferences sharedPreferences =
						PreferenceManager.getDefaultSharedPreferences(context);
				boolean sentToken = sharedPreferences.getBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, false);
				if (sentToken) {
					mInformationTextView.setText(getString(R.string.gcm_send_message));
				} else {
					mInformationTextView.setText(getString(R.string.token_error_message));
				}
			}
		};
		mInformationTextView = (TextView) findViewById(R.id.informationTextView);

		// Registering BroadcastReceiver
		registerReceiver();

		if (checkPlayServices()) {
			// Start IntentService to register this application with GCM.
			Intent intent = new Intent(this, RegistrationIntentService.class);
			startService(intent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver();
	}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		isReceiverRegistered = false;
		super.onPause();
	}

	/**
	 * Local BR 등록 로직.
	 */
	private void registerReceiver(){
		if(!isReceiverRegistered) {
			LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
					new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
			isReceiverRegistered = true;
		}
	}
	/**
	 * Google Play Services APK 설치 여부 확인 로직.
	 */
	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Snackbar.make(mInformationTextView, "This device is not supported.", Snackbar.LENGTH_SHORT).show();
				mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
			}
			return false;
		}
		return true;
	}
}
