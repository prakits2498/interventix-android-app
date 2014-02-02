package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.entity.Utente;

public class GetUtenteAsyncTack extends AsyncTask<Long, Void, Utente> {

	private final Context context;

	public GetUtenteAsyncTack(Context context) {

		this.context = context;
	}

	@Override
	protected Utente doInBackground(Long... params) {

		ContentResolver cr = context.getContentResolver();

		String[] projection = new String[] { Fields._ID, UtenteDB.Fields.CANCELLATO, UtenteDB.Fields.CESTINATO, UtenteDB.Fields.COGNOME, UtenteDB.Fields.CONFLITTO,
				UtenteDB.Fields.EMAIL, UtenteDB.Fields.ID_UTENTE, UtenteDB.Fields.NOME, UtenteDB.Fields.PASSWORD, UtenteDB.Fields.REVISIONE, UtenteDB.Fields.TIPO,
				UtenteDB.Fields.USERNAME };

		String selection = Fields.TYPE + " = ? AND " + UtenteDB.Fields.ID_UTENTE + " = ?";

		String[] selectionArgs = new String[] { UtenteDB.UTENTE_ITEM_TYPE, "" + params[0] };

		Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);

		Utente utente = new Utente();

		if (cursor.moveToFirst()) {

			utente.setCancellato(cursor.getInt(cursor.getColumnIndex(UtenteDB.Fields.CANCELLATO)) == 1 ? true : false);
			utente.setCestinato(cursor.getInt(cursor.getColumnIndex(UtenteDB.Fields.CESTINATO)) == 1 ? true : false);
			utente.setCognome(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.COGNOME)));
			utente.setConflitto(cursor.getInt(cursor.getColumnIndex(UtenteDB.Fields.CONFLITTO)) == 1 ? true : false);
			utente.setEmail(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.EMAIL)));
			utente.setIdUtente(cursor.getLong(cursor.getColumnIndex(UtenteDB.Fields.ID_UTENTE)));
			utente.setNome(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.NOME)));
			utente.setPassword(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.PASSWORD)));
			utente.setRevisione(cursor.getLong(cursor.getColumnIndex(UtenteDB.Fields.REVISIONE)));
			utente.setTipo(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.TIPO)));
			utente.setUserName(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.USERNAME)));
		}

		if (!cursor.isClosed())
			cursor.close();

		return utente;
	}

	@Override
	protected void onPostExecute(Utente result) {

	}
}
