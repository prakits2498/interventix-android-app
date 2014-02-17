package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
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

		String[] projection = new String[] { Fields._ID, DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, DettaglioInterventoDB.Fields.DESCRIZIONE,
				DettaglioInterventoDB.Fields.FINE, DettaglioInterventoDB.Fields.INIZIO, DettaglioInterventoDB.Fields.OGGETTO, DettaglioInterventoDB.Fields.TIPO,
				DettaglioInterventoDB.Fields.INTERVENTO, DettaglioInterventoDB.Fields.TECNICI };

		String selection = Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";

		String[] selectionArgs = new String[] { DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + params[0] };

		Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);

		DettaglioIntervento dettInterv = new DettaglioIntervento();

		if (cursor.moveToFirst()) {

			dettInterv.iddettagliointervento = (params[0]);
			dettInterv.descrizione = (cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.DESCRIZIONE)));
			dettInterv.fine = (cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.FINE)));
			dettInterv.inizio = (cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.INIZIO)));
			dettInterv.oggetto = (cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.OGGETTO)));
			dettInterv.idintervento = (cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.INTERVENTO)));
			dettInterv.tipo = (cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TIPO)));
			dettInterv.tecniciintervento = (cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.TECNICI)));
		}

		if (!cursor.isClosed())
			cursor.close();

		return dettInterv;
	}

	@Override
	protected void onPostExecute(DettaglioIntervento result) {

	}
}
