package com.federicocolantoni.projects.interventix.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by federico on 19/05/13.
 */
public class DBHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "Interventix_DB";
	private final static int DB_VERSION = 1;

	private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ DBContract.Data.DB_TABLE + "(" + DBContract.Data.Fields._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DBContract.Data.Fields.TYPE + " INTEGER NOT NULL, ";

	static {
		for (String data : DBContract.Data.Fields.DATA) {
			CREATE_TABLE += data + " TEXT, ";
		}

		for (String blob : DBContract.Data.Fields.BLOB) {
			CREATE_TABLE += blob + " BLOB, ";
		}

		CREATE_TABLE += DBContract.Data.Fields.INDEX + " TEXT)";
	}

	private final static String[] CREATE_INDEXES = {
			"CREATE INDEX " + DBContract.Data.Fields.INDEX + " ON "
					+ DBContract.Data.DB_TABLE + "("
					+ DBContract.Data.Fields.INDEX + ")",
			"CREATE INDEX " + DBContract.Data.Fields.TYPE + " ON "
					+ DBContract.Data.DB_TABLE + "("
					+ DBContract.Data.Fields.TYPE + ")" };

	private final static String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ DBContract.Data.DB_TABLE;

	private final static String[] DROP_INDEXES = {
			"DROP INDEX " + DBContract.Data.Fields.INDEX,
			"DROP INDEX " + DBContract.Data.Fields.TYPE };

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {

		sqLiteDatabase.execSQL(CREATE_TABLE);
		for (String sql : CREATE_INDEXES) {
			sqLiteDatabase.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {

		if (newVersion > oldVersion) {
			sqLiteDatabase.execSQL(DROP_TABLE);
			for (String sql : DROP_INDEXES) {
				sqLiteDatabase.execSQL(sql);
			}
		}
		onCreate(sqLiteDatabase);
	}

	public DBHelper(Context context) {

		super(context, DB_NAME, null, DB_VERSION);
	}
}
