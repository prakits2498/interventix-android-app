package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.models.Cliente;

public class GetClienteAsyncTask extends AsyncTask<Long, Void, Cliente> {

    private Context context;

    public GetClienteAsyncTask(Context context) {

	this.context = context;
    }

    @Override
    protected Cliente doInBackground(Long... params) {

	ContentResolver cr = context.getContentResolver();

	String[] projection =
		new String[] {
			Fields._ID, ClienteDB.Fields.CANCELLATO, ClienteDB.Fields.CITTA, ClienteDB.Fields.CODICE_FISCALE, ClienteDB.Fields.CONFLITTO, ClienteDB.Fields.EMAIL, ClienteDB.Fields.FAX,
			ClienteDB.Fields.ID_CLIENTE, ClienteDB.Fields.INDIRIZZO, ClienteDB.Fields.INTERNO, ClienteDB.Fields.MODIFICATO, ClienteDB.Fields.NOMINATIVO, ClienteDB.Fields.NOTE,
			ClienteDB.Fields.PARTITAIVA, ClienteDB.Fields.REFERENTE, ClienteDB.Fields.REVISIONE, ClienteDB.Fields.TELEFONO, ClienteDB.Fields.UFFICIO
		};

	String selection = Fields.TYPE + " = ? AND " + ClienteDB.Fields.ID_CLIENTE + " = ?";

	String[] selectionArgs = new String[] {
		ClienteDB.CLIENTE_ITEM_TYPE, "" + params[0]
	};

	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);

	Cliente cliente = new Cliente();

	if (cursor.moveToFirst()) {

	    cliente.cancellato = (cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CANCELLATO)) == 1 ? true : false);
	    cliente.citta = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CITTA)));
	    cliente.codicefiscale = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CODICE_FISCALE)));
	    cliente.conflitto = (cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CONFLITTO)) == 1 ? true : false);
	    cliente.email = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.EMAIL)));
	    cliente.fax = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.FAX)));
	    cliente.indirizzo = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INDIRIZZO)));
	    cliente.interno = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INTERNO)));
	    cliente.nominativo = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
	    cliente.note = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOTE)));
	    cliente.partitaiva = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.PARTITAIVA)));
	    cliente.referente = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.REFERENTE)));
	    cliente.revisione = (cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.REVISIONE)));
	    cliente.telefono = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.TELEFONO)));
	    cliente.ufficio = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.UFFICIO)));
	}

	if (!cursor.isClosed())
	    cursor.close();

	return cliente;
    }

    @Override
    protected void onPostExecute(Cliente result) {

    }
}
