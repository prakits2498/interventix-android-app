package com.federicocolantoni.projects.interventix.task;

import java.math.BigDecimal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.models.Intervento;

public class GetCostsInterventoAsyncTask extends AsyncTask<Long, Void, Intervento> {

    private final Context mContext;

    public GetCostsInterventoAsyncTask(Context context) {

	mContext = context;
    }

    @Override
    protected Intervento doInBackground(Long... params) {

	ContentResolver cr = mContext.getContentResolver();

	String[] projection =
		new String[] {
			Fields._ID, InterventoDB.Fields.COSTO_MANODOPERA, InterventoDB.Fields.COSTO_COMPONENTI, InterventoDB.Fields.COSTO_ACCESSORI, InterventoDB.Fields.IMPORTO,
			InterventoDB.Fields.IVA, InterventoDB.Fields.TOTALE
		};

	String selection = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";

	String[] selectionArgs = new String[] {
		InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0]
	};

	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);

	Intervento costsIntervento = new Intervento();

	if (cursor.moveToFirst()) {

	    costsIntervento.costomanodopera = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_MANODOPERA))));
	    costsIntervento.costocomponenti = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_COMPONENTI))));
	    costsIntervento.costoaccessori = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_ACCESSORI))));
	    costsIntervento.importo = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IMPORTO))));
	    costsIntervento.iva = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IVA))));
	    costsIntervento.totale = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.TOTALE))));
	}

	if (!cursor.isClosed())
	    cursor.close();

	return costsIntervento;
    }
}
