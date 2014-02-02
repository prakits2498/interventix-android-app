package com.federicocolantoni.projects.interventix.task;

import java.math.BigDecimal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.Constants;
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

		Intervento intervento = new Intervento();

		if (cursor.moveToFirst()) {
			intervento.setCancellato(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CANCELLATO)) == 1 ? true : false);
			intervento.setChiuso(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CHIUSO)) == 1 ? true : false);
			intervento.setCostoAccessori(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_ACCESSORI))));
			intervento.setCostoComponenti(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_COMPONENTI))));
			intervento.setCostoManodopera(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_MANODOPERA))));
			intervento.setDataOra(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA)));
			intervento.setFirma(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.FIRMA)));
			intervento.setIdCliente(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.CLIENTE)));
			intervento.setIdIntervento(params[0]);
			intervento.setIdTecnico(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.TECNICO)));
			intervento.setImporto(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IMPORTO))));
			intervento.setIdIntervento(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO)));
			intervento.setIva(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IVA))));
			intervento.setModalita(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODALITA)));
			intervento.setMotivo(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MOTIVO)));
			intervento.setNominativo(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOMINATIVO)));
			intervento.setNote(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOTE)));
			intervento.setNumeroIntervento(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.NUMERO_INTERVENTO)));
			intervento.setNuovo((cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NUOVO)) != null && cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NUOVO))
					.equals(Constants.INTERVENTO_NUOVO)) ? true : false);
			intervento.setProdotto(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.PRODOTTO)));
			intervento.setRifFattura(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.RIFERIMENTO_FATTURA)));
			intervento.setRifScontrino(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.RIFERIMENTO_SCONTRINO)));
			intervento.setSaldato(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.SALDATO)) == 1 ? true : false);
			intervento.setTipologia(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.TIPOLOGIA)));
			intervento.setTotale(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.TOTALE))));
		}

		if (!cursor.isClosed())
			cursor.close();

		return intervento;
	}

	@Override
	protected void onPostExecute(Intervento result) {

	}
}
