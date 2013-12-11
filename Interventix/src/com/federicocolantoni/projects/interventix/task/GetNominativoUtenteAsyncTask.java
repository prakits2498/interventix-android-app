package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.intervento.Utente;

public class GetNominativoUtenteAsyncTask extends AsyncTask<Long, Void, Utente> {
    
    private Context context;
    
    public GetNominativoUtenteAsyncTask(Context context) {
	this.context = context.getApplicationContext();
    }
    
    @Override
    protected Utente doInBackground(Long... params) {
	
	ContentResolver cr = context.getContentResolver();
	
	String[] projection = new String[] {
		UtenteDB.Fields._ID, UtenteDB.Fields.NOME, UtenteDB.Fields.COGNOME
	};
	
	String selection = UtenteDB.Fields.TYPE + " =? AND " + UtenteDB.Fields.ID_UTENTE + " = ?";
	
	String[] selectionArgs = new String[] {
		UtenteDB.UTENTE_ITEM_TYPE, "" + params[0]
	};
	
	Cursor cursor = cr.query(UtenteDB.CONTENT_URI, projection, selection, selectionArgs, null);
	
	Utente utente = new Utente();
	
	if (cursor.moveToFirst()) {
	    utente.setmNome(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.NOME)));
	    utente.setmCognome(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.COGNOME)));
	}
	
	if (!cursor.isClosed()) {
	    cursor.close();
	}
	else {
	    System.out.println("Cursor for " + this.getClass().getSimpleName() + " is closed");
	}
	
	return utente;
    }
    
    @Override
    protected void onPostExecute(Utente result) {
	
    }
}