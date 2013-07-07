
package com.federicocolantoni.projects.interventix.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.DBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class GetInformationsIntervento extends
	AsyncTask<Long, Void, Intervento> {

    private Context mContext;

    public GetInformationsIntervento(Context context) {

	mContext = context.getApplicationContext();
    }

    @Override
    protected Intervento doInBackground(Long... params) {

	ContentResolver cr = mContext.getContentResolver();

	String[] projection = new String[] { InterventoDB.Fields._ID,
		InterventoDB.Fields.TIPOLOGIA, InterventoDB.Fields.MODALITA,
		InterventoDB.Fields.PRODOTTO, InterventoDB.Fields.MOTIVO,
		InterventoDB.Fields.NOMINATIVO, InterventoDB.Fields.DATA_ORA };

	String selection = InterventoDB.Fields.TYPE + " = ? AND "
		+ InterventoDB.Fields.ID_INTERVENTO + " = ?";

	String[] selectionArgs = new String[] {
		InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0] };

	Cursor cursor = cr.query(InterventoDB.CONTENT_URI, projection,
		selection, selectionArgs, null);

	Intervento informationsIntervento = new Intervento();

	if (cursor.moveToFirst()) {

	    informationsIntervento.setmTipologia(cursor.getString(cursor
		    .getColumnIndex(InterventoDB.Fields.TIPOLOGIA)));
	    informationsIntervento.setmModalita(cursor.getString(cursor
		    .getColumnIndex(InterventoDB.Fields.MODALITA)));
	    informationsIntervento.setmMotivo(cursor.getString(cursor
		    .getColumnIndex(InterventoDB.Fields.MOTIVO)));
	    informationsIntervento.setmProdotto(cursor.getString(cursor
		    .getColumnIndex(InterventoDB.Fields.PRODOTTO)));
	    informationsIntervento.setmNominativo(cursor.getString(cursor
		    .getColumnIndex(InterventoDB.Fields.NOMINATIVO)));
	    informationsIntervento.setmDataOra(cursor.getLong(cursor
		    .getColumnIndex(InterventoDB.Fields.DATA_ORA)));
	}

	if (!cursor.isClosed()) {
	    cursor.close();
	} else {
	    System.out
		    .println("Cursor for GetInformationsIntervento is closed");
	}

	return informationsIntervento;
    }

    @Override
    protected void onPostExecute(Intervento result) {

    }
}
