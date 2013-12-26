package com.federicocolantoni.projects.interventix.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.utils.ListDetailsIntervento;

public class GetListaDettagliInterventoAsyncTask extends AsyncTask<Long, Void, ListDetailsIntervento> {
    
    private final Context mContext;
    
    public GetListaDettagliInterventoAsyncTask(Context context) {
	mContext = context.getApplicationContext();
    }
    
    @Override
    protected ListDetailsIntervento doInBackground(Long... params) {
	
	ContentResolver cr = mContext.getContentResolver();
	
	String[] projection = new String[] {
		DettaglioInterventoDB.Fields._ID,
		DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO,
		DettaglioInterventoDB.Fields.INTERVENTO,
		DettaglioInterventoDB.Fields.DESCRIZIONE,
		DettaglioInterventoDB.Fields.OGGETTO,
		DettaglioInterventoDB.Fields.TIPO,
		DettaglioInterventoDB.Fields.INIZIO,
		DettaglioInterventoDB.Fields.FINE,
		DettaglioInterventoDB.Fields.MODIFICATO,
		DettaglioInterventoDB.Fields.TECNICI
	};
	
	String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.INTERVENTO + " = ?";
	
	String[] selectionArgs = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + params[0]
	};
	
	Cursor cursor = cr.query(DettaglioInterventoDB.CONTENT_URI, projection, selection, selectionArgs, null);
	
	List<DettaglioIntervento> listaDettagliInterv = new ArrayList<DettaglioIntervento>();
	
	while (cursor.moveToNext()) {
	    
	    DettaglioIntervento detailInterv = new DettaglioIntervento();
	    
	    detailInterv.setIdDettaglioIntervento(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO)));
	    detailInterv.setIntervento(params[0]);
	    detailInterv.setDescrizione(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.DESCRIZIONE)));
	    detailInterv.setOggetto(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.OGGETTO)));
	    detailInterv.setTipo(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TIPO)));
	    detailInterv.setInizio(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.INIZIO)));
	    detailInterv.setFine(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.FINE)));
	    detailInterv.setModificato(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.MODIFICATO)));
	    try {
		detailInterv.setTecnici(new JSONArray(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TECNICI))));
	    }
	    catch (JSONException e) {
		
		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }
	    
	    listaDettagliInterv.add(detailInterv);
	}
	
	if (!cursor.isClosed()) {
	    cursor.close();
	}
	else {
	    System.out.println("Cursor for GetDettaglioIntervento is closed");
	}
	
	ListDetailsIntervento listDetails = new ListDetailsIntervento();
	listDetails.setListDetails(listaDettagliInterv);
	
	return listDetails;
    }
}
