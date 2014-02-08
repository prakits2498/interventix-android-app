package com.federicocolantoni.projects.interventix.task;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.entity.Cliente;

public class GetClientiAsyncTask extends AsyncTask<Void, Void, ArrayList<Cliente>> {
    
    private Context mContext;
    
    public GetClientiAsyncTask(Context context) {
    
	mContext = context;
    }
    
    @Override
    protected ArrayList<Cliente> doInBackground(Void... params) {
    
	ArrayList<Cliente> listaClienti = new ArrayList<Cliente>();
	
	String[] PROJECTION = new String[] {
	Fields._ID, ClienteDB.Fields.CANCELLATO, ClienteDB.Fields.CITTA, ClienteDB.Fields.CODICE_FISCALE, ClienteDB.Fields.CONFLITTO, ClienteDB.Fields.EMAIL, ClienteDB.Fields.FAX, ClienteDB.Fields.ID_CLIENTE, ClienteDB.Fields.INDIRIZZO, ClienteDB.Fields.INTERNO, ClienteDB.Fields.NOMINATIVO, ClienteDB.Fields.NOTE, ClienteDB.Fields.PARTITAIVA, ClienteDB.Fields.REFERENTE, ClienteDB.Fields.REVISIONE, ClienteDB.Fields.TELEFONO, ClienteDB.Fields.UFFICIO
	};
	
	String SELECTION = Fields.TYPE + "=?";
	
	String[] SELECTION_ARGS = new String[] {
	    ClienteDB.CLIENTE_ITEM_TYPE
	};
	
	String sortOrder = ClienteDB.Fields.NOMINATIVO + " ASC";
	
	ContentResolver cr = mContext.getContentResolver();
	
	Cursor cursor = cr.query(Data.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, sortOrder);
	
	if (cursor.moveToFirst()) {
	    
	    while (cursor.moveToNext()) {
		
		Cliente cliente = new Cliente();
		cliente.idcliente = cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.ID_CLIENTE));
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
		
		listaClienti.add(cliente);
	    }
	}
	
	if (!cursor.isClosed())
	    cursor.close();
	
	return listaClienti;
    }
    
    @Override
    protected void onPostExecute(ArrayList<Cliente> result) {
    
	InterventoController.listaClienti = result;
    }
}
