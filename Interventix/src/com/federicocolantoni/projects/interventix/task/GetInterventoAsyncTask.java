package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.entity.Intervento;

public class GetInterventoAsyncTask extends AsyncTask<Long, Void, Intervento> {

	private final Context context;

	public GetInterventoAsyncTask(Context context) {

		this.context = context.getApplicationContext();
	}

	@Override
	protected Intervento doInBackground(Long... params) {

		ContentResolver cr = context.getContentResolver();

		String[] projection = new String[] { Fields._ID, InterventoDB.Fields.CANCELLATO, InterventoDB.Fields.CHIUSO, InterventoDB.Fields.CLIENTE, InterventoDB.Fields.CONFLITTO,
				InterventoDB.Fields.COSTO_ACCESSORI, InterventoDB.Fields.COSTO_COMPONENTI, InterventoDB.Fields.COSTO_MANODOPERA, InterventoDB.Fields.DATA_ORA,
				InterventoDB.Fields.FIRMA, InterventoDB.Fields.ID_INTERVENTO, InterventoDB.Fields.IMPORTO, InterventoDB.Fields.IVA, InterventoDB.Fields.MODALITA,
				InterventoDB.Fields.MODIFICATO, InterventoDB.Fields.MOTIVO, InterventoDB.Fields.NOMINATIVO, InterventoDB.Fields.NOTE, InterventoDB.Fields.NUMERO_INTERVENTO,
				InterventoDB.Fields.NUOVO, InterventoDB.Fields.PRODOTTO, InterventoDB.Fields.RIFERIMENTO_FATTURA, InterventoDB.Fields.RIFERIMENTO_SCONTRINO,
				InterventoDB.Fields.SALDATO, InterventoDB.Fields.TECNICO, InterventoDB.Fields.TIPOLOGIA, InterventoDB.Fields.TOTALE };

		String selection = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";

		String[] selectionArgs = new String[] { InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0] };

		Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);
		cursor.moveToFirst();

		Intervento intervento = Intervento.getFromCursor(cursor);

		if (!cursor.isClosed())
			cursor.close();

		return intervento;
	}

	@Override
	protected void onPostExecute(Intervento result) {

	}
}
