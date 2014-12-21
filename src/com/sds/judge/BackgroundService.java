package com.sds.judge;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/*run this service always and check currently running app every LOOP_SECONDS.*/
public class BackgroundService extends Service {

	protected static final int LOOP_SECONDS = 3;// calculate running apps after
												// every LOOP_SECONDS interval
	private static final int NUMBER_RUNNING_TASK = 1;// get this number of
														// running apps at a
														// time
	String launcherPackage = null;// Launcher is the name given to the part of
									// the Android user interface that lets
									// users customize the home screen (e.g. the
									// phone's desktop)
	Handler handler = new Handler();
	private PowerManager powerManager;// check screen on/off
	public static UsageDB db = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (db == null) {
			db = new UsageDB(this);
			db.open();
		} else {
			return;
		}
	}

	@Override
	public void onDestroy() {
		Log.e("onDestroy service..", "true");
		handler.removeCallbacks(runnable);
		db.close();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		// get all installed packages
		StartUsageAnalysis startUsageAnalysis = new StartUsageAnalysis(
				getApplicationContext());
		startUsageAnalysis.execute();

		launcherPackage = getLauncherPackage();
		Log.e("launcher", "" + launcherPackage);
		handler.removeCallbacks(runnable);
		handler.post(runnable);

		return START_STICKY;
	}

	private String getLauncherPackage() {
		// TODO Auto-generated method stub

		final Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		final ResolveInfo res = getApplicationContext().getPackageManager()
				.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
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

		Runnable runnable = new Runnable() {
			public void run() {

				try {
					Log.e("in try", "true");
					powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
					final ActivityManager activityManager = (ActivityManager) getApplicationContext()
							.getSystemService(Context.ACTIVITY_SERVICE);
					final List<ActivityManager.RunningTaskInfo> runningApps = activityManager
							.getRunningTasks(NUMBER_RUNNING_TASK);

					String runningAppPackage = "";

					if (runningApps != null && runningApps.size() > 0) {

						ComponentName componentName = runningApps.get(0).baseActivity;
						runningAppPackage = componentName.getPackageName();
						Log.e("runningTask package", "" + runningAppPackage);
					}
					// add time when there is running app except the launcher
					// and screen is on.
					if (runningAppPackage != null && launcherPackage != null
							&& !launcherPackage.equals(runningAppPackage)) {
						if (powerManager.isScreenOn()) {

							Log.e("finally update", "time");
							if (db.containApp(runningAppPackage)) {
								db.addSeconds(runningAppPackage, LOOP_SECONDS);
							} else {
								AppDetails appDetails = new AppDetails();
								appDetails.packageName = runningAppPackage;
								appDetails.seconds = LOOP_SECONDS;
								db.addApp(appDetails);
								Log.e("package name: " + appDetails.packageName,
										"time: " + appDetails.seconds);
							}
						}
					}

				} catch (Exception e) {
					Log.e("in catch", "-" + e.getMessage());
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable).start();

	}
}
