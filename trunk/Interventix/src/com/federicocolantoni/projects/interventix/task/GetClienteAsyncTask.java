package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.entity.Cliente;

public class GetClienteAsyncTask extends AsyncTask<Long, Void, Cliente> {
    
    private Context context;
    
    public GetClienteAsyncTask(Context context) {
    
	this.context = context;
    }
    
    @Override
    protected Cliente doInBackground(Long... params) {
    
	ContentResolver cr = context.getContentResolver();
	
	String[] projection = new String[] {
	Fields._ID, ClienteDB.Fields.CANCELLATO, ClienteDB.Fields.CITTA, ClienteDB.Fields.CODICE_FISCALE, ClienteDB.Fields.CONFLITTO, ClienteDB.Fields.EMAIL, ClienteDB.Fields.FAX, ClienteDB.Fields.ID_CLIENTE, ClienteDB.Fields.INDIRIZZO, ClienteDB.Fields.INTERNO, ClienteDB.Fields.MODIFICATO, ClienteDB.Fields.NOMINATIVO, ClienteDB.Fields.NOTE, ClienteDB.Fields.PARTITAIVA, ClienteDB.Fields.REFERENTE, ClienteDB.Fields.REVISIONE, ClienteDB.Fields.TELEFONO, ClienteDB.Fields.UFFICIO
	};
	
	String selection = Fields.TYPE + " = ? AND " + ClienteDB.Fields.ID_CLIENTE + " = ?";
	
	String[] selectionArgs = new String[] {
	ClienteDB.CLIENTE_ITEM_TYPE, "" + params[0]
	};
	
	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);
	
	Cliente cliente = new Cliente();
	
	if (cursor.moveToFirst()) {
	    
	    cliente.setCancellato(cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CANCELLATO)) == 1 ? true : false);
	    cliente.setCitta(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CITTA)));
	    cliente.setCodiceFiscale(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CODICE_FISCALE)));
	    cliente.setConflitto(cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CONFLITTO)) == 1 ? true : false);
	    cliente.setEmail(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.EMAIL)));
	    cliente.setFax(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.FAX)));
	    cliente.setIdCliente(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.ID_CLIENTE)));
	    cliente.setIndirizzo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INDIRIZZO)));
	    cliente.setInterno(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INTERNO)));
	    cliente.setNominativo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
	    cliente.setNote(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOTE)));
	    cliente.setPartitaIVA(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.PARTITAIVA)));
	    cliente.setReferente(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.REFERENTE)));
	    cliente.setRevisione(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.REVISIONE)));
	    cliente.setTelefono(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.TELEFONO)));
	    cliente.setUfficio(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.UFFICIO)));
	}
	
	if (!cursor.isClosed())
	    cursor.close();
	
	return cliente;
    }
    
    @Override
    protected void onPostExecute(Cliente result) {
    
    }
}
