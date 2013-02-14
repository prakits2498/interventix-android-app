
package com.federicocolantoni.projects.interventix.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class InterventixContentProvider extends ContentProvider {

    private InterventixDBHelper mDBHelper;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

	SQLiteDatabase db = mDBHelper.getWritableDatabase();

	if (selection == null) {
	    selection = "1";
	}

	int deleteCount = db.delete(InterventixAPI.Cliente.PATH, selection,
		selectionArgs);

	getContext().getContentResolver().notifyChange(uri, null);

	return deleteCount;
    }

    @Override
    public String getType(Uri uri) {

	switch (DBDataConstants.MATCHER.match(uri)) {
	    case DBDataConstants.CLIENT_COLLECTION:
		return InterventixAPI.Cliente.TYPE;
	    case DBDataConstants.CLIENT_ITEM:
		return InterventixAPI.Cliente.ITEM_TYPE;
	    default:
		return null;
	}
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

	if (DBDataConstants.MATCHER.match(uri) == DBDataConstants.CLIENT_COLLECTION) {
	    final SQLiteDatabase db = mDBHelper.getWritableDatabase();
	    long idItem = db.insert(InterventixAPI.Cliente.PATH, null, values);

	    if (idItem != -1) {
		Uri u = ContentUris.withAppendedId(uri, idItem);
		getContext().getContentResolver().notifyChange(u, null);
		return u;
	    } else {
		return null;
	    }
	}
	throw unsupportedUri(uri);
    }

    @Override
    public boolean onCreate() {

	mDBHelper = new InterventixDBHelper(getContext());
	return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
	    String[] selectionArgs, String sortOrder) {

	final int match = DBDataConstants.MATCHER.match(uri);
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	qb.setTables(InterventixAPI.Cliente.PATH);

	switch (match) {
	    case DBDataConstants.CLIENT_COLLECTION:
		break;
	    case DBDataConstants.CLIENT_ITEM:
		qb.appendWhere("_id = " + uri.getLastPathSegment());
		break;
	    default:
		unsupportedUri(uri);
	}

	SQLiteDatabase db = mDBHelper.getReadableDatabase();
	Cursor cursor = qb.query(db, projection, selection, selectionArgs,
		null, null, sortOrder);

	cursor.setNotificationUri(getContext().getContentResolver(), uri);
	return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
	    String[] selectionArgs) {

	if (DBDataConstants.MATCHER.match(uri) == DBDataConstants.CLIENT_ITEM) {
	    final String where;
	    if (TextUtils.isEmpty(selection)) {
		where = "_id = " + ContentUris.parseId(uri);
	    } else {
		where = "_id = " + ContentUris.parseId(uri) + " AND ("
			+ selection + ")";
	    }

	    SQLiteDatabase db = mDBHelper.getWritableDatabase();

	    final int updated = db.update(InterventixAPI.Cliente.PATH, values,
		    where, selectionArgs);
	    if (updated > 0) {
		// Se ci sono stati degli update (al piu' 1) notifichiamo
		// l'update
		getContext().getContentResolver().notifyChange(uri, null);
	    }

	    return updated;
	} else {
	    throw unsupportedUri(uri);
	}
    }

    private static UnsupportedOperationException unsupportedUri(Uri uri) {

	return new UnsupportedOperationException("URI: " + uri
		+ " is not supported");
    }
}
