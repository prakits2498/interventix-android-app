package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.models.Utente;

public class GetNominativoUtenteAsyncTask extends AsyncTask<Long, Void, Utente> {

    private Context context;

    public GetNominativoUtenteAsyncTask(Context context) {

	this.context = context.getApplicationContext();
    }

    @Override
    protected Utente doInBackground(Long... params) {

	ContentResolver cr = context.getContentResolver();

	String[] projection = new String[] {
		Fields._ID, UtenteDB.Fields.NOME, UtenteDB.Fields.COGNOME
	};

	String selection = Fields.TYPE + " =? AND " + UtenteDB.Fields.ID_UTENTE + " = ?";

	String[] selectionArgs = new String[] {
		UtenteDB.UTENTE_ITEM_TYPE, "" + params[0]
	};

	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);

	Utente utente = new Utente();

	if (cursor.moveToFirst()) {
	    utente.nome = (cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.NOME)));
	    utente.cognome = (cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.COGNOME)));
	}

	if (!cursor.isClosed())
	    cursor.close();

	return utente;
    }

    @Override
    protected void onPostExecute(Utente result) {

    }
}
