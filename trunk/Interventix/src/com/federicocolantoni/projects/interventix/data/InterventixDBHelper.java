
package com.federicocolantoni.projects.interventix.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InterventixDBHelper extends SQLiteOpenHelper {

    public InterventixDBHelper(Context context) {

	super(context, DBDataConstants.DATABASE_NAME, null,
		DBDataConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

	db.execSQL(DBDataConstants.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	if (newVersion > oldVersion) {
	    db.execSQL(DBDataConstants.TABLE_DROP);
	}
	onCreate(db);
    }
}
