
package com.federicocolantoni.projects.interventix.utils;

import java.math.BigDecimal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.DBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class GetCostsIntervento extends AsyncTask<Long, Void, Intervento> {

    private Context mContext;

    public GetCostsIntervento(Context context) {

	mContext = context;
    }

    @Override
    protected Intervento doInBackground(Long... params) {

	ContentResolver cr = mContext.getContentResolver();

	String[] projection = new String[] { InterventoDB.Fields._ID,
		InterventoDB.Fields.COSTO_MANODOPERA,
		InterventoDB.Fields.COSTO_COMPONENTI,
		InterventoDB.Fields.COSTO_ACCESSORI,
		InterventoDB.Fields.IMPORTO, InterventoDB.Fields.IVA,
		InterventoDB.Fields.TOTALE };

	String selection = InterventoDB.Fields.TYPE + " = ? AND "
		+ InterventoDB.Fields.ID_INTERVENTO + " = ?";

	String[] selectionArgs = new String[] {
		InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0] };

	Cursor cursor = cr.query(InterventoDB.CONTENT_URI, projection,
		selection, selectionArgs, null);

	Intervento costsIntervento = new Intervento();

	if (cursor.moveToFirst()) {

	    costsIntervento
		    .setmCostoManodopera(BigDecimal.valueOf(cursor.getLong(cursor
			    .getColumnIndex(InterventoDB.Fields.COSTO_MANODOPERA))));
	    costsIntervento
		    .setmCostoComponenti(BigDecimal.valueOf(cursor.getLong(cursor
			    .getColumnIndex(InterventoDB.Fields.COSTO_COMPONENTI))));
	    costsIntervento
		    .setmCostoAccessori(BigDecimal.valueOf(cursor.getLong(cursor
			    .getColumnIndex(InterventoDB.Fields.COSTO_ACCESSORI))));
	    costsIntervento
		    .setmImporto(BigDecimal.valueOf(cursor.getLong(cursor
			    .getColumnIndex(InterventoDB.Fields.IMPORTO))));
	    costsIntervento.setmIva(BigDecimal.valueOf(cursor.getLong(cursor
		    .getColumnIndex(InterventoDB.Fields.IVA))));
	    costsIntervento.setmTotale(BigDecimal.valueOf(cursor.getLong(cursor
		    .getColumnIndex(InterventoDB.Fields.TOTALE))));
	}

	if (!cursor.isClosed()) {
	    cursor.close();
	} else {
	    System.out.println("Cursor for " + this.getClass().getSimpleName()
		    + " is closed");
	}

	return costsIntervento;
    }
}
