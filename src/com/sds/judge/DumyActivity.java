package com.sds.judge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

//activity to start service for time usage calculation of various apps.
public class DumyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_packages);

		// start service for time usage calculation
		Intent i = new Intent(DumyActivity.this, BackgroundService.class);
		DumyActivity.this.startService(i);

	}
}
