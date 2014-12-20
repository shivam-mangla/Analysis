package com.sds.judge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DumyActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("dumy activity starts","true");
		setContentView(R.layout.application_packages);
		Intent i= new Intent(this, BackgroundService.class);
		this.startService(i); 

		}

	
}
