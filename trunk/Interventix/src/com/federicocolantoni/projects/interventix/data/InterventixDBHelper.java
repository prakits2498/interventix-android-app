package com.federicocolantoni.projects.interventix.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by federico on 19/05/13.
 */
public class InterventixDBHelper extends SQLiteOpenHelper {
    
    private final static String DB_NAME = "Interventix_DB";
    private final static int DB_VERSION = 1;
    
    private static String CREATE_TABLE_INTERVENTIX = "CREATE TABLE IF NOT EXISTS " + InterventixDBContract.Data.DB_TABLE + "(" + InterventixDBContract.Data.Fields._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + InterventixDBContract.Data.Fields.TYPE + " INTEGER NOT NULL, ";
    
    static {
	for (String data : InterventixDBContract.Data.Fields.DATA) {
	    CREATE_TABLE_INTERVENTIX += data + " TEXT, ";
	}
	
	for (String blob : InterventixDBContract.Data.Fields.BLOB) {
	    CREATE_TABLE_INTERVENTIX += blob + " BLOB, ";
	}
	
	CREATE_TABLE_INTERVENTIX += InterventixDBContract.Data.Fields.INDEX + " TEXT)";
	
    }
    
    private final static String[] CREATE_INDEXES = {
	    "CREATE INDEX " + InterventixDBContract.Data.Fields.INDEX + " ON " + InterventixDBContract.Data.DB_TABLE + "(" + InterventixDBContract.Data.Fields.INDEX + ")",
	    "CREATE INDEX " + InterventixDBContract.Data.Fields.TYPE + " ON " + InterventixDBContract.Data.DB_TABLE + "(" + InterventixDBContract.Data.Fields.TYPE + ")"
    };
    
    private final static String DROP_TABLE = "DROP TABLE IF EXISTS " + InterventixDBContract.Data.DB_TABLE;
    
    private final static String[] DROP_INDEXES = {
	    "DROP INDEX " + InterventixDBContract.Data.Fields.INDEX,
	    "DROP INDEX " + InterventixDBContract.Data.Fields.TYPE
    };
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
	
	sqLiteDatabase.execSQL(CREATE_TABLE_INTERVENTIX);
	
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
    
    public InterventixDBHelper(Context context) {
	
	super(context, DB_NAME, null, DB_VERSION);
    }
}
