package com.federicocolantoni.projects.interventix.task;

import java.math.BigDecimal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.entity.Intervento;

public class GetOverviewInterventoAsyncTask extends AsyncTask<Long, Void, Intervento> {

	private final Context mContext;

	public GetOverviewInterventoAsyncTask(Context context) {

		mContext = context.getApplicationContext();
	}

	@Override
	protected Intervento doInBackground(Long... params) {

		ContentResolver cr = mContext.getContentResolver();

		String[] projection = new String[] { Fields._ID, InterventoDB.Fields.CLIENTE, InterventoDB.Fields.DATA_ORA, InterventoDB.Fields.FIRMA, InterventoDB.Fields.TOTALE };

		String selection = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";

		String[] selectionArgs = new String[] { InterventoDB.INTERVENTO_ITEM_TYPE, "" + params[0] };

		Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);

		Intervento overviewIntervento = new Intervento();

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				overviewIntervento.setFirma(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.FIRMA)));
				overviewIntervento.setIdCliente(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.CLIENTE)));
				overviewIntervento.setTotale(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.TOTALE))));
				overviewIntervento.setDataOra(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA)));
			}

			if (!cursor.isClosed()) {
				cursor.close();
			} else {
				System.out.println("Cursor for " + this.getClass().getSimpleName() + " is closed");
			}
		}

		return overviewIntervento;
	}

	@Override
	protected void onPostExecute(Intervento result) {

	}
}
