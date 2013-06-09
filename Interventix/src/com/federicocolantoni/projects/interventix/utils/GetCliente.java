
package com.federicocolantoni.projects.interventix.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.DBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.intervento.Cliente;

public class GetCliente extends AsyncTask<Long, Void, Cliente> {

    private Context context;

    public GetCliente(Context context) {

	this.context = context.getApplicationContext();
    }

    @Override
    protected Cliente doInBackground(Long... params) {

	ContentResolver cr = context.getContentResolver();

	String selection = ClienteDB.Fields.TYPE + "='"
		+ ClienteDB.CLIENTE_ITEM_TYPE + "' AND "
		+ ClienteDB.Fields.ID_CLIENTE + "=" + params[0];

	Cursor cursor = cr.query(ClienteDB.CONTENT_URI, null, selection, null,
		null);

	Cliente cliente = new Cliente();

	if (cursor.moveToFirst()) {
	    cliente.setmNominativo(cursor.getString(cursor
		    .getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
	}

	if (!cursor.isClosed()) {
	    cursor.close();
	} else {
	    System.out.println("Cursor for GetCliente is closed");
	}

	return cliente;
    }

    @Override
    protected void onPostExecute(Cliente result) {

    }
}
