package com.federicocolantoni.projects.interventix.task;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;

public class GetDettaglioInterventoAsyncTask extends AsyncTask<Long, Void, DettaglioIntervento> {
    
    private final Context context;
    
    public GetDettaglioInterventoAsyncTask(Context context) {
	this.context = context.getApplicationContext();
    }
    
    @Override
    protected DettaglioIntervento doInBackground(Long... params) {
	
	ContentResolver cr = context.getContentResolver();
	
	String[] projection = new String[] {
		DettaglioInterventoDB.Fields._ID,
		DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO,
		DettaglioInterventoDB.Fields.DESCRIZIONE,
		DettaglioInterventoDB.Fields.FINE,
		DettaglioInterventoDB.Fields.INIZIO,
		DettaglioInterventoDB.Fields.OGGETTO,
		DettaglioInterventoDB.Fields.TIPO,
		DettaglioInterventoDB.Fields.INTERVENTO,
		DettaglioInterventoDB.Fields.TECNICI
	};
	
	String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
	
	String[] selectionArgs = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + params[0]
	};
	
	Cursor cursor = cr.query(DettaglioInterventoDB.CONTENT_URI, projection, selection, selectionArgs, null);
	
	DettaglioIntervento dettInterv = new DettaglioIntervento();
	
	if (cursor.moveToFirst()) {
	    
	    dettInterv.setIdDettaglioIntervento(params[0]);
	    dettInterv.setDescrizione(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.DESCRIZIONE)));
	    dettInterv.setFine(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.FINE)));
	    dettInterv.setInizio(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.INIZIO)));
	    dettInterv.setOggetto(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.OGGETTO)));
	    dettInterv.setIntervento(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.INTERVENTO)));
	    dettInterv.setTipo(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TIPO)));
	    try {
		dettInterv.setTecnici(new JSONArray(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TECNICI))));
	    }
	    catch (JSONException e) {
		
		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }
	}
	
	if (!cursor.isClosed()) {
	    cursor.close();
	}
	else {
	    System.out.println("Cursor for GetDettaglioIntervento is closed");
	}
	
	return dettInterv;
    }
    
    @Override
    protected void onPostExecute(DettaglioIntervento result) {
	
    }
}
