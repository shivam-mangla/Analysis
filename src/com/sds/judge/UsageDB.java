package com.sds.judge;

import java.text.ParseException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//database for storing time usage of applications (time since installation in seconds)
public class UsageDB {

	private DbHelper dbHelper;
	private Context context;
	private SQLiteDatabase database;
	private String[] allColumns = { DbHelper.KEY_ID_DB,
			DbHelper.KEY_PACKAGE_NAME, DbHelper.KEY_TIME };

	public UsageDB(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new DbHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean isEmpty() {

		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ DbHelper.DATABASE_TABLE, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
			return false;
		} else {
			cursor.close();
			return true;
		}

	}

	public boolean containApp(AppDetails lappDetails) {
		if (!isEmpty()) {
			Cursor cursor = database.query(DbHelper.DATABASE_TABLE, null,
					DbHelper.KEY_PACKAGE_NAME + "='" + lappDetails.packageName
							+ "'",// remember single quotes
					null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
		}
		return false;
	}

	public boolean containApp(String packageName) {
		if (!isEmpty()) {
			Cursor cursor = database.query(DbHelper.DATABASE_TABLE, null,
					DbHelper.KEY_PACKAGE_NAME + "='" + packageName + "'", null,
					null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
		}
		return false;
	}

	// update time usage for app already in db
	public void addSeconds(String packageName, int deltaTime) {
		try {
			AppDetails appDetails = getAppDetails(packageName);
			appDetails.seconds += deltaTime;
			database.execSQL("UPDATE " + DbHelper.DATABASE_TABLE + " SET "
					+ DbHelper.KEY_TIME + " = " + appDetails.seconds
					+ " WHERE " + DbHelper.KEY_PACKAGE_NAME + "='"
					+ packageName + "'");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("add seconds", e.getMessage());
		}

	}

	public void addApp(AppDetails lappDetails) {

		ContentValues cv = new ContentValues();
		cv.put(DbHelper.KEY_PACKAGE_NAME, lappDetails.packageName);
		cv.put(DbHelper.KEY_TIME, lappDetails.seconds);
		database.insert(DbHelper.DATABASE_TABLE, null, cv);
		Log.e("query add app ", "successful");
	}

	public AppDetails getAppDetails(String packageName) {
		Cursor cursor = database.query(DbHelper.DATABASE_TABLE, null,
				DbHelper.KEY_PACKAGE_NAME + "='" + packageName + "'", null,
				null, null, null);
		try {
			while (cursor.moveToFirst()) {
				AppDetails appDetails = cursorToAppDetails(cursor);
				return appDetails;
			}
		} catch (ParseException e) {
		} finally {
			cursor.close();
		}
		return null;
	}

	private AppDetails cursorToAppDetails(Cursor cursor) throws ParseException {
		// TODO Auto-generated method stub
		AppDetails appDetails = new AppDetails();
		appDetails.packageName = cursor.getString(1);
		appDetails.seconds = cursor.getInt(2);
		return appDetails;
	}
}
