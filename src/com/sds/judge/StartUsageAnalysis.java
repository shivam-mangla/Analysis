package com.sds.judge;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


public class StartUsageAnalysis {

	static PackageManager packagemanager;
	List<PackageInfo> packageList;
	Context mContext = null;

	public StartUsageAnalysis(Context mContext) {
		this.mContext = mContext;
	}

	public void execute() {
		// TODO Auto-generated method stub
		//Globals.allApplications = new ArrayList<AppDetails>();
		if (BackgroundService.db == null) {
			BackgroundService.db = new UsageDB(mContext);
			BackgroundService.db.open();
			Log.e("db open","check");
		} else {
			return;
		}
		packagemanager = mContext.getPackageManager();
		packageList = packagemanager
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		for (PackageInfo pi : packageList) {
			boolean b = isSystemPackage(pi);
			@SuppressWarnings("unused")
			String[] permission = (pi.requestedPermissions);
			if (!b) {
				try {
					AppDetails appDetails = new AppDetails();
					appDetails.packageName = pi.packageName;
					appDetails.milliseconds = 0;
					if(!BackgroundService.db.containApp(appDetails))
					{
						BackgroundService.db.addApp(appDetails);
						
					}
				//	Globals.allApplications.add(appDetails);

				} catch (Exception e) {
				}
			}
		}
		Log.e("collect package name","start service");


	}

	private boolean isSystemPackage(PackageInfo pi) {
		// TODO Auto-generated method stub
		return ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
				: false;

	}

}
