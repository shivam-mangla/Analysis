package com.sds.judge;

import java.util.ArrayList;

public class Globals {

	public static ArrayList<AppDetails> allApplications;

	public static boolean containApp(String lpackageName) {
		if (allApplications.size() > 0) {

			for (AppDetails app_details : allApplications) {
				if (app_details.packageName == lpackageName) {
					return true;
				}
			}
		}
		return false;

	}
	
	public static AppDetails getAppDetails(String lpackageName)
	{
		if(allApplications.size()>0)
		{
			for (AppDetails app_details : allApplications) {
				if (app_details.packageName == lpackageName) {
					return app_details;
				}
			}
		}
		return null;
	}
	
	public static void addApp(AppDetails lappDetails)
	{
		allApplications.add(lappDetails);
	}
}
