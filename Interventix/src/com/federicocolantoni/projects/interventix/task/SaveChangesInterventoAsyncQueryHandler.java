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
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.fragments.CostsInterventoFragment;
import com.federicocolantoni.projects.interventix.fragments.InformationsInterventoFragment;

@SuppressLint("NewApi")
public class SaveChangesInterventoAsyncQueryHandler extends AsyncQueryHandler {
    
    private Context context;
    
    public SaveChangesInterventoAsyncQueryHandler(Context context) {
	
	super(context.getContentResolver());
	this.context = context.getApplicationContext();
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
	    case CostsInterventoFragment.TOKEN_COSTO_MANODOPERA:
		
		ContentValues values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		String modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(CostsInterventoFragment.TOKEN_COSTO_MANODOPERA + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    InterventoDB.INTERVENTO_ITEM_TYPE,
			    "" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(CostsInterventoFragment.TOKEN_COSTO_MANODOPERA, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(CostsInterventoFragment.TOKEN_COSTO_MANODOPERA + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case CostsInterventoFragment.TOKEN_COSTO_COMPONENTI:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(CostsInterventoFragment.TOKEN_COSTO_COMPONENTI + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    InterventoDB.INTERVENTO_ITEM_TYPE,
			    "" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(CostsInterventoFragment.TOKEN_COSTO_COMPONENTI, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(CostsInterventoFragment.TOKEN_COSTO_COMPONENTI + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case CostsInterventoFragment.TOKEN_COSTO_ACCESSORI:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(CostsInterventoFragment.TOKEN_COSTO_ACCESSORI + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    InterventoDB.INTERVENTO_ITEM_TYPE,
			    "" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(CostsInterventoFragment.TOKEN_COSTO_ACCESSORI, null, Data.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(CostsInterventoFragment.TOKEN_COSTO_ACCESSORI + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case InformationsInterventoFragment.TOKEN_INFO_TIPOLOGIA:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_TIPOLOGIA + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			"" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(InformationsInterventoFragment.TOKEN_INFO_TIPOLOGIA, null, Data.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_TIPOLOGIA + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case InformationsInterventoFragment.TOKEN_INFO_MODALITA:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_MODALITA + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			"" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(InformationsInterventoFragment.TOKEN_INFO_MODALITA, null, Data.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_MODALITA + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case InformationsInterventoFragment.TOKEN_INFO_PRODOTTO:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_PRODOTTO + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			"" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(InformationsInterventoFragment.TOKEN_INFO_PRODOTTO, null, Data.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_PRODOTTO + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case InformationsInterventoFragment.TOKEN_INFO_NOMINATIVO:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_NOMINATIVO + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			"" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(InformationsInterventoFragment.TOKEN_INFO_NOMINATIVO, null, Data.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_NOMINATIVO + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case InformationsInterventoFragment.TOKEN_INFO_MOTIVO:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_MOTIVO + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			"" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(InformationsInterventoFragment.TOKEN_INFO_MOTIVO, null, Data.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_MOTIVO + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			edit.commit();
		}
		
		break;
	    
	    case InformationsInterventoFragment.TOKEN_INFO_DATA_ORA:
		
		values = (ContentValues) cookie;
		
		cursor.moveToFirst();
		
		modified = cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO));
		
		if (modified.equals("N")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_DATA_ORA + " - L'intervento non è stato ancora modificato.");
		    
		    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			"" + cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO))
		    };
		    
		    startUpdate(InformationsInterventoFragment.TOKEN_INFO_DATA_ORA, null, Data.CONTENT_URI, values, selection, selectionArgs);
		}
		else if (modified.equals("M")) {
		    
		    System.out.println(InformationsInterventoFragment.TOKEN_INFO_DATA_ORA + " - L'intervento è stato già modificato.");
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.INTERV_MODIFIED, true);
		    
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
