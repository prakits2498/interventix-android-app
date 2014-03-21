package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.models.Intervento;

public class GetInformationsInterventoAsyncTask extends AsyncTask<Long, Void, Intervento> {

    private Context mContext;

    public GetInformationsInterventoAsyncTask(Context context) {

	mContext = context.getApplicationContext();
    }

    @Override
    protected Intervento doInBackground(Long... params) {

	ContentResolver cr = mContext.getContentResolver();

	String[] projection =
		new String[] {
			Fields._ID, InterventoDB.Fields.TIPOLOGIA, InterventoDB.Fields.MODALITA, InterventoDB.Fields.PRODOTTO, InterventoDB.Fields.MOTIVO, InterventoDB.Fields.NOMINATIVO,
			InterventoDB.Fields.DATA_ORA
		};

	String selection = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";

	String[] selectionArgs = new String[] {
		InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0]
	};

	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);

	Intervento informationsIntervento = new Intervento();

	if (cursor.moveToFirst()) {

	    informationsIntervento.tipologia = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.TIPOLOGIA)));
	    informationsIntervento.modalita = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODALITA)));
	    informationsIntervento.motivo = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MOTIVO)));
	    informationsIntervento.prodotto = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.PRODOTTO)));
	    informationsIntervento.nominativo = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOMINATIVO)));
	    informationsIntervento.dataora = (cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA)));
	}

	if (!cursor.isClosed())
	    cursor.close();

	return informationsIntervento;
    }

    @Override
    protected void onPostExecute(Intervento result) {

    }
}
