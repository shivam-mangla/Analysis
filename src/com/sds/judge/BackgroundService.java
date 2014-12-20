package com.sds.judge;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class BackgroundService extends Service {

	protected static final int LOOP_SECONDS = 3;
	private static final int NUMBER_RUNNING_TASK = 1;
	String launcherPackage = null;
	Handler handler = new Handler();
	private PowerManager powerManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		StartUsageAnalysis startUsageAnalysis= new StartUsageAnalysis(getApplicationContext());
		startUsageAnalysis.execute();
	
		Log.e("service","oncreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.e("service","onstartcommand");
		// receiver
		launcherPackage=getLauncherPackage();
		handler.removeCallbacks(runnable);
		handler.post(runnable);

		return START_STICKY;
	}

	private String getLauncherPackage() {
		// TODO Auto-generated method stub
		final Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		final ResolveInfo res = getApplicationContext().getPackageManager().resolveActivity(
				intent, 0);
		if (res.activityInfo == null) {
			// should not happen. A home is always installed, isn't it?
		}
		if ("android".equals(res.activityInfo.packageName)) {
			// No default selected
		} else {
			return res.activityInfo.packageName;
		}
		return null;
	}

	protected final Runnable runnable = new Runnable() {
		public void run() {
			updateTime();
			handler.postDelayed(this, LOOP_SECONDS * 1000);
		}


	};
	private void updateTime() {
		// TODO Auto-generated method stub
		
		//turn to runnable 
		Log.e("update time","enter runnable");
		Runnable runnable= new Runnable()
		{
			public void run() {
				
			try{
				Log.e("in try","true");
	       powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		   final ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
           final List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(NUMBER_RUNNING_TASK);
           String runningTaskPackage="";
   		Log.e("activity manager", "" + activityManager);
   		if (runningTasks == null)
			Log.e("recent tasks ", "null");

           
           if (runningTasks != null && runningTasks.size() > 0) {
        	  
               ComponentName componentName = runningTasks.get(0).baseActivity;
               runningTaskPackage = componentName.getPackageName();
               Log.e("runningTask package",""+runningTaskPackage);
           }
           
           
   		if (launcherPackage == null)
			Log.e("launcher", "null");

           if(runningTaskPackage!=null&& launcherPackage!=null && !launcherPackage.equals(runningTaskPackage)){
        	Log.e(" first if ","cleared"); 
        	if(powerManager.isScreenOn()){
        		
        		Log.e("finally update","time");
        	   if(Globals.containApp(runningTaskPackage)){
        		   Globals.getAppDetails(runningTaskPackage).milliseconds+=LOOP_SECONDS;
        	   }
        	   else{
        		   AppDetails appDetails= new AppDetails();
        		   appDetails.packageName= runningTaskPackage;
        		   appDetails.milliseconds=LOOP_SECONDS;
        		   Globals.addApp(appDetails);
        		   Log.e("package name: "+appDetails.packageName,"time: "+appDetails.milliseconds);
            	   
        	   }
        	   for(AppDetails ad:Globals.allApplications)
        	   {
        		   //Log.e("package name: "+ad.packageName,"time: "+ad.milliseconds);
        	   }
        	   
        	}
           }
           
			}catch( Exception e)
			{
				Log.e("in catch","true-"+e.getMessage());
				e.printStackTrace();
			
			}
           
		}
	};
	 new Thread(runnable).start();
	
           
        
	}
}
