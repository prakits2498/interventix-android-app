package com.federicocolantoni.projects.interventix.task;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.intervento.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.utils.ListDetailsIntervento;

public class GetDettagliInterventoAsyncTask extends AsyncTask<Long, Void, ListDetailsIntervento> {
    
    private final Context mContext;
    
    public GetDettagliInterventoAsyncTask(Context context) {
	mContext = context.getApplicationContext();
    }
    
    @Override
    protected ListDetailsIntervento doInBackground(Long... params) {
	
	ContentResolver cr = mContext.getContentResolver();
	
	String[] projection = new String[] {
		Fields._ID,
		DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO,
		DettaglioInterventoDB.Fields.INTERVENTO,
		DettaglioInterventoDB.Fields.DESCRIZIONE,
		DettaglioInterventoDB.Fields.OGGETTO,
		DettaglioInterventoDB.Fields.TIPO,
		DettaglioInterventoDB.Fields.INIZIO,
		DettaglioInterventoDB.Fields.FINE,
		DettaglioInterventoDB.Fields.TECNICI
	};
	
	String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.INTERVENTO + " = ?";
	
	String[] selectionArgs = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
		"" + params[0]
	};
	
	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);
	
	List<DettaglioIntervento> listaDettagliInterv = new ArrayList<DettaglioIntervento>();
	
	if (cursor.moveToFirst()) {
	    
	    DettaglioIntervento detailInterv = new DettaglioIntervento();
	    
	    detailInterv.setmIdDettaglioIntervento(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO)));
	    detailInterv.setmIntervento(params[0]);
	    detailInterv.setmDescrizione(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.DESCRIZIONE)));
	    detailInterv.setmOggetto(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.OGGETTO)));
	    detailInterv.setmTipo(cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TIPO)));
	    detailInterv.setmInizio(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.INIZIO)));
	    detailInterv.setmFine(cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.FINE)));
	    String tecnici = cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TECNICI));
	    
	    if (tecnici.length() > 0) {
		System.out.println("Tecnici dettaglio intervento: " + tecnici);
		
		String[] split = tecnici.split(",");
		
		List<Integer> listTecnici = new ArrayList<Integer>();
		
		for (String element : split) {
		    
		    listTecnici.add(Integer.parseInt(element));
		}
		
		detailInterv.setmTecnici(listTecnici);
	    }
	    else {
		
		System.out.println("Nessun tecnico per questo dettaglio intervento");
		
		detailInterv.setmTecnici(new ArrayList<Integer>());
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
    
    @Override
    protected void onPostExecute(ListDetailsIntervento result) {
	
    }
}
