package com.federicocolantoni.projects.interventix.task;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SaveChangesDettaglioInterventoAsyncQueryHandler extends AsyncQueryHandler {
    
    public SaveChangesDettaglioInterventoAsyncQueryHandler(Context context) {
	super(context.getContentResolver());
    }
    
    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
	
	super.onDeleteComplete(token, cookie, result);
    }
    
    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
	
	super.onInsertComplete(token, cookie, uri);
    }
    
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
	
	super.onQueryComplete(token, cookie, cursor);
    }
    
    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
	
	super.onUpdateComplete(token, cookie, result);
    }
}
