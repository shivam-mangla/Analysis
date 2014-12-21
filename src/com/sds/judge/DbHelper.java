package com.sds.judge;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{

	public static final String KEY_ID_DB = "id";
	public static final String KEY_PACKAGE_NAME = "package_name";
	public static final String KEY_TIME = "time";
	
	public static String DATABASE_NAME = Environment
			.getExternalStorageDirectory() + "/Analysis.db";
	public static final String DATABASE_TABLE = "usage";
	public static final int DATABASE_VERSION = 1;

	
	
	  private static final String DATABASE_CREATE = "create virtual table "
	            + DATABASE_TABLE +  " USING fts3 " + "(" + KEY_ID_DB
	            + " integer primary key autoincrement, " + KEY_PACKAGE_NAME
	            + " text not null, "  + KEY_TIME
	            + " integer not null default 0);";

	 
	  
	  
	private static DbHelper instance;

	public static synchronized DbHelper getDbHelper(Context context) {
		if (instance == null)
			instance = new DbHelper(context);
		return instance;
	}

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		  db.execSQL(DATABASE_CREATE);
		  Log.e("database created","true");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w("Database", "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		onCreate(db);
	}
	

}
