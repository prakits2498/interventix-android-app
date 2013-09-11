package com.federicocolantoni.projects.interventix.task;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.fragments.DetailInterventoFragment;

@SuppressLint("NewApi")
public class SaveChangesDettaglioInterventoAsyncQueryHandler extends AsyncQueryHandler {
    
    private Context context;
    
    public SaveChangesDettaglioInterventoAsyncQueryHandler(Context context) {
	super(context.getContentResolver());
	
	this.context = context;
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
	
	switch (token) {
	
	    case DetailInterventoFragment.TOKEN_DESCRIZIONE_DETTAGLIO:
		
		ContentValues values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		String modified = cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_DESCRIZIONE_DETTAGLIO + " - Il dettaglio dell'intervento non è stato ancora modificato.");
		    
		    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
			    "" + cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO))
		    };
		    
		    startUpdate(DetailInterventoFragment.TOKEN_DESCRIZIONE_DETTAGLIO, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_DESCRIZIONE_DETTAGLIO + " - Il dettaglio dell'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case DetailInterventoFragment.TOKEN_OGGETTO_DETTAGLIO:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_OGGETTO_DETTAGLIO + " - Il dettaglio dell'intervento non è stato ancora modificato.");
		    
		    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
			    "" + cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO))
		    };
		    
		    startUpdate(DetailInterventoFragment.TOKEN_OGGETTO_DETTAGLIO, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_OGGETTO_DETTAGLIO + " - Il dettaglio dell'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case DetailInterventoFragment.TOKEN_TIPO_DETTAGLIO:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_TIPO_DETTAGLIO + " - Il dettaglio dell'intervento non è stato ancora modificato.");
		    
		    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
			    "" + cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO))
		    };
		    
		    startUpdate(DetailInterventoFragment.TOKEN_TIPO_DETTAGLIO, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_TIPO_DETTAGLIO + " - Il dettaglio dell'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case DetailInterventoFragment.TOKEN_ORA_FINE_DETTAGLIO:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_ORA_FINE_DETTAGLIO + " - Il dettaglio dell'intervento non è stato ancora modificato.");
		    
		    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
			    "" + cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO))
		    };
		    
		    startUpdate(DetailInterventoFragment.TOKEN_ORA_FINE_DETTAGLIO, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_ORA_FINE_DETTAGLIO + " - Il dettaglio dell'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case DetailInterventoFragment.TOKEN_ORA_INIZIO_DETTAGLIO:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_ORA_INIZIO_DETTAGLIO + " - Il dettaglio dell'intervento non è stato ancora modificato.");
		    
		    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
			    "" + cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO))
		    };
		    
		    startUpdate(DetailInterventoFragment.TOKEN_ORA_INIZIO_DETTAGLIO, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(DetailInterventoFragment.TOKEN_ORA_INIZIO_DETTAGLIO + " - Il dettaglio dell'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	}
    }
    
    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
	
	super.onUpdateComplete(token, cookie, result);
    }
}
