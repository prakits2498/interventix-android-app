package com.federicocolantoni.projects.interventix.task;

import java.math.BigDecimal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class GetInterventoAsyncTask extends AsyncTask<Long, Void, Intervento> {
    
    private final Context context;
    
    public GetInterventoAsyncTask(Context context) {
	
	this.context = context.getApplicationContext();
    }
    
    @Override
    protected Intervento doInBackground(Long... params) {
	
	ContentResolver cr = context.getContentResolver();
	
	String[] projection = new String[] {
		InterventoDB.Fields._ID, InterventoDB.Fields.CANCELLATO,
		InterventoDB.Fields.CHIUSO, InterventoDB.Fields.CLIENTE,
		InterventoDB.Fields.COSTO_ACCESSORI,
		InterventoDB.Fields.COSTO_COMPONENTI,
		InterventoDB.Fields.COSTO_MANODOPERA,
		InterventoDB.Fields.DATA_ORA, InterventoDB.Fields.FIRMA,
		InterventoDB.Fields.ID_INTERVENTO, InterventoDB.Fields.IMPORTO,
		InterventoDB.Fields.IVA, InterventoDB.Fields.MODALITA,
		InterventoDB.Fields.MODIFICATO, InterventoDB.Fields.MOTIVO,
		InterventoDB.Fields.NOMINATIVO, InterventoDB.Fields.NOTE,
		InterventoDB.Fields.NUMERO_INTERVENTO,
		InterventoDB.Fields.PRODOTTO,
		InterventoDB.Fields.RIFERIMENTO_FATTURA,
		InterventoDB.Fields.RIFERIMENTO_SCONTRINO,
		InterventoDB.Fields.SALDATO, InterventoDB.Fields.TECNICO,
		InterventoDB.Fields.TIPOLOGIA, InterventoDB.Fields.TOTALE
	};
	
	String selection = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	
	String[] selectionArgs = new String[] {
		InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0]
	};
	
	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);
	
	Intervento intervento = new Intervento();
	
	if (cursor.moveToFirst()) {
	    intervento.setmCancellato(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CANCELLATO)) == 1 ? true : false);
	    intervento.setmChiuso(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CHIUSO)) == 1 ? true : false);
	    intervento.setmCostoAccessori(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_ACCESSORI))));
	    intervento.setmCostoComponenti(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_COMPONENTI))));
	    intervento.setmCostoManodopera(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_MANODOPERA))));
	    intervento.setmDataOra(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA)));
	    intervento.setmFirma(cursor.getBlob(cursor.getColumnIndex(InterventoDB.Fields.FIRMA)));
	    intervento.setmIdCliente(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.CLIENTE)));
	    intervento.setmIdIntervento(params[0]);
	    intervento.setmIdTecnico(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.TECNICO)));
	    intervento.setmImporto(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IMPORTO))));
	    intervento.setmIdIntervento(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO)));
	    intervento.setmIva(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IVA))));
	    intervento.setmModalita(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODALITA)));
	    intervento.setmModificato(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO)));
	    intervento.setmMotivo(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MOTIVO)));
	    intervento.setmNominativo(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOMINATIVO)));
	    intervento.setmNote(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOTE)));
	    intervento.setmNumeroIntervento(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.NUMERO_INTERVENTO)));
	    intervento.setmProdotto(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.PRODOTTO)));
	    intervento.setmRifFattura(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.RIFERIMENTO_FATTURA)));
	    intervento.setmRifScontrino(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.RIFERIMENTO_SCONTRINO)));
	    intervento.setmSaldato(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.SALDATO)) == 1 ? true : false);
	    intervento.setmTipologia(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.TIPOLOGIA)));
	    intervento.setmTotale(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.TOTALE))));
	}
	
	if (!cursor.isClosed()) {
	    cursor.close();
	}
	else {
	    System.out.println("Cursor for GetIntervento is closed");
	}
	
	return intervento;
    }
    
    @Override
    protected void onPostExecute(Intervento result) {
	
    }
}
