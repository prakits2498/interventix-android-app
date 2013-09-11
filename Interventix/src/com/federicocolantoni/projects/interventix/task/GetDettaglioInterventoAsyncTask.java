package com.federicocolantoni.projects.interventix.task;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.intervento.DettaglioIntervento;

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
		DettaglioInterventoDB.Fields.DESCRIZIONE,
		DettaglioInterventoDB.Fields.FINE,
		DettaglioInterventoDB.Fields.INIZIO,
		DettaglioInterventoDB.Fields.OGGETTO,
		DettaglioInterventoDB.Fields.TIPO,
		DettaglioInterventoDB.Fields.TECNICI
	};
	
	String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
	
	String[] selectionArgs = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
		"" + params[0]
	};
	
	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);
	
	DettaglioIntervento dettInterv = new DettaglioIntervento();
	
	if (cursor.moveToFirst()) {
	    
	    dettInterv.setmDescrizione(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.DESCRIZIONE)));
	    dettInterv.setmFine(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.FINE)));
	    dettInterv.setmInizio(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.INIZIO)));
	    dettInterv.setmOggetto(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.OGGETTO)));
	    dettInterv.setmTipo(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TIPO)));
	    
	    String tecnici = cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TECNICI));
	    
	    List<Integer> listTecnici = new ArrayList<Integer>();
	    
	    if (tecnici.length() > 0) {
		String[] idTecnici = tecnici.split(",");
		
		for (String element : idTecnici) {
		    listTecnici.add(Integer.parseInt(element));
		}
	    }
	    
	    dettInterv.setmTecnici(listTecnici);
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
