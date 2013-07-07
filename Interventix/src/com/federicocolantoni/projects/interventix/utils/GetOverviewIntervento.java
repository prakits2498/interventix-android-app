
package com.federicocolantoni.projects.interventix.utils;

import java.math.BigDecimal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.DBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class GetOverviewIntervento extends AsyncTask<Long, Void, Intervento> {

    private Context mContext;

    public GetOverviewIntervento(Context context) {

	this.mContext = context.getApplicationContext();
    }

    @Override
    protected Intervento doInBackground(Long... params) {

	ContentResolver cr = mContext.getContentResolver();

	String[] projection = new String[] { InterventoDB.Fields._ID,
		InterventoDB.Fields.CLIENTE, InterventoDB.Fields.DATA_ORA,
		InterventoDB.Fields.FIRMA, InterventoDB.Fields.TOTALE };

	String selection = InterventoDB.Fields.TYPE + " = ? AND "
		+ InterventoDB.Fields.ID_INTERVENTO + " = ?";

	String[] selectionArgs = new String[] {
		InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0] };

	Cursor cursor = cr.query(InterventoDB.CONTENT_URI, projection,
		selection, selectionArgs, null);

	Intervento overviewIntervento = new Intervento();

	if (cursor.moveToFirst()) {

	    overviewIntervento.setmFirma(cursor.getBlob(cursor
		    .getColumnIndex(InterventoDB.Fields.FIRMA)));
	    overviewIntervento.setmIdCliente(cursor.getLong(cursor
		    .getColumnIndex(InterventoDB.Fields.CLIENTE)));
	    overviewIntervento.setmTotale(BigDecimal.valueOf(cursor
		    .getDouble(cursor
			    .getColumnIndex(InterventoDB.Fields.TOTALE))));
	    overviewIntervento.setmDataOra(cursor.getLong(cursor
		    .getColumnIndex(InterventoDB.Fields.DATA_ORA)));
	}

	if (!cursor.isClosed()) {
	    cursor.close();
	} else {
	    System.out.println("Cursor for GetOverviewIntervento is closed");
	}

	return overviewIntervento;
    }

    @Override
    protected void onPostExecute(Intervento result) {

    }
}
