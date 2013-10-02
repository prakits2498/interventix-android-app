package com.federicocolantoni.projects.interventix.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;

/**
 * Created by federico on 19/05/13.
 */
public class InterventixProvider extends ContentProvider {
    
    private final static int SINGLE_ITEM = 0;
    private final static int COLLECTION = 1;
    private final static UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    
    static {
	MATCHER.addURI(InterventixDBContract.AUTHORITY, InterventixDBContract.Data.PATH, COLLECTION);
	MATCHER.addURI(InterventixDBContract.AUTHORITY, InterventixDBContract.Data.PATH + "/#", SINGLE_ITEM);
    }
    
    private static InterventixDBHelper mDBHelper;
    
    @Override
    public boolean onCreate() {
	mDBHelper = new InterventixDBHelper(getContext());
	return mDBHelper != null;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
	
	String where = null;
	final int match = MATCHER.match(uri);
	if (match == SINGLE_ITEM) {
	    where = Fields._ID + " = " + uri.getLastPathSegment();
	}
	else
	    if (match != COLLECTION)
		throw new UnsupportedOperationException("URI " + uri + " not supported!");
	
	SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	queryBuilder.setTables(Data.DB_TABLE);
	if (where != null) {
	    queryBuilder.appendWhere(where);
	}
	
	SQLiteDatabase db = mDBHelper.getReadableDatabase();
	
	final Cursor result = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	result.setNotificationUri(getContext().getContentResolver(), uri);
	return result;
    }
    
    @Override
    public String getType(Uri uri) {
	switch (MATCHER.match(uri)) {
	    case SINGLE_ITEM:
		return InterventixDBContract.Data.SINGLE_ITEM_TYPE;
	    case COLLECTION:
		return InterventixDBContract.Data.COLLECTION_TYPE;
	    default:
		throw new IllegalArgumentException("URI " + uri + " not supported!");
	}
    }
    
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
	SQLiteDatabase db = mDBHelper.getWritableDatabase();
	final long id = db.insert(Data.DB_TABLE, null, contentValues);
	
	Uri result = null;
	if (id > -1) {
	    result = ContentUris.withAppendedId(Data.CONTENT_URI, id);
	    getContext().getContentResolver().notifyChange(result, null);
	}
	return result;
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
	final int match = MATCHER.match(uri);
	if (match == SINGLE_ITEM) {
	    selection = Fields._ID + " = " + uri.getLastPathSegment() + (TextUtils.isEmpty(selection) ? "" : "AND (" + selection + ")");
	}
	else
	    if (match != COLLECTION)
		throw new UnsupportedOperationException("URI " + uri + " not supported!");
	
	if (TextUtils.isEmpty(selection)) {
	    selection = "1"; // no selection means we have to delete everything
	}
	
	SQLiteDatabase db = mDBHelper.getWritableDatabase();
	final int deleted = db.delete(Data.DB_TABLE, selection, selectionArgs);
	
	if (deleted > 0) {
	    getContext().getContentResolver().notifyChange(uri, null);
	}
	
	return deleted;
    }
    
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
	final int match = MATCHER.match(uri);
	if (match == SINGLE_ITEM) {
	    selection = Fields._ID + " = " + uri.getLastPathSegment() + (TextUtils.isEmpty(selection) ? "" : "AND (" + selection + ")");
	}
	else
	    if (match != COLLECTION)
		throw new UnsupportedOperationException("URI " + uri + " not supported!");
	
	SQLiteDatabase db = mDBHelper.getWritableDatabase();
	final int updated = db.update(Data.DB_TABLE, contentValues, selection, selectionArgs);
	
	if (updated > 0) {
	    getContext().getContentResolver().notifyChange(uri, null);
	}
	
	return updated;
    }
}
