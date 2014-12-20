package com.sds.judge;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class ApplicationPackages extends Activity{
	static PackageManager packagemanager;
	List<PackageInfo> packageList;
	List<PackageInfo> packageList1;
	
	TextView tv,size;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_packages);
		tv= (TextView) findViewById( R.id.tvPackages);
		size= (TextView) findViewById( R.id.tvSize);
		packageList1 = new ArrayList<PackageInfo>();
		packagemanager = getPackageManager();
		packageList = packagemanager
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		packageList1 = new ArrayList<PackageInfo>();
		for (PackageInfo pi : packageList) {
			boolean b = isSystemPackage(pi);

			@SuppressWarnings("unused")
			String[] permission = (pi.requestedPermissions);
			if (!b) {
				try {
					packageList1.add(pi);
					
				} catch (Exception e) {
				}
			}
		}
	
	
		String s="";
		for(int i=0;i<packageList1.size();i++)
			s+= (packageList1.get(i)+"  ");
		size.setText(packageList1.size()+"");
		tv.setText(s);
		
	}
	private boolean isSystemPackage(PackageInfo pi) {
		// TODO Auto-generated method stub
		return ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
				: false;

	}

}
