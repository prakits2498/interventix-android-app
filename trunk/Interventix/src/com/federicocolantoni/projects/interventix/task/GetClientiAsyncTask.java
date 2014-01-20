package com.federicocolantoni.projects.interventix.task;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.entity.Cliente;

public class GetClientiAsyncTask extends AsyncTask<Void, Void, ArrayList<Cliente>> {
    
    private Context mContext;
    private Handler mHandler;
    
    public GetClientiAsyncTask(Context context, Handler handler) {
    
	mContext = context;
	mHandler = handler;
    }
    
    @Override
    protected ArrayList<Cliente> doInBackground(Void... params) {
    
	ArrayList<Cliente> listaClienti = new ArrayList<Cliente>();
	
	String[] PROJECTION = new String[] {
	ClienteDB.Fields._ID, ClienteDB.Fields.CANCELLATO, ClienteDB.Fields.CITTA, ClienteDB.Fields.CODICE_FISCALE, ClienteDB.Fields.CONFLITTO, ClienteDB.Fields.EMAIL, ClienteDB.Fields.FAX, ClienteDB.Fields.ID_CLIENTE, ClienteDB.Fields.INDIRIZZO, ClienteDB.Fields.INTERNO, ClienteDB.Fields.NOMINATIVO, ClienteDB.Fields.NOTE, ClienteDB.Fields.PARTITAIVA, ClienteDB.Fields.REFERENTE, ClienteDB.Fields.REVISIONE, ClienteDB.Fields.TELEFONO, ClienteDB.Fields.UFFICIO
	};
	
	String SELECTION = ClienteDB.Fields.TYPE + "=?";
	
	String[] SELECTION_ARGS = new String[] {
	    ClienteDB.CLIENTE_ITEM_TYPE
	};
	
	String sortOrder = ClienteDB.Fields.NOMINATIVO + " ASC";
	
	ContentResolver cr = mContext.getContentResolver();
	
	Cursor cursor = cr.query(ClienteDB.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, sortOrder);
	
	if (cursor.moveToFirst()) {
	    
	    while (cursor.moveToNext()) {
		
		Cliente cliente = new Cliente(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.ID_CLIENTE)));
		cliente.setCancellato(cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CANCELLATO)) == 1 ? true : false);
		cliente.setCitta(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CITTA)));
		cliente.setCodiceFiscale(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CODICE_FISCALE)));
		cliente.setConflitto(cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CONFLITTO)) == 1 ? true : false);
		cliente.setEmail(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.EMAIL)));
		cliente.setFax(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.FAX)));
		cliente.setIndirizzo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INDIRIZZO)));
		cliente.setInterno(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INTERNO)));
		cliente.setNominativo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
		cliente.setNote(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOTE)));
		cliente.setPartitaIVA(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.PARTITAIVA)));
		cliente.setReferente(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.REFERENTE)));
		cliente.setRevisione(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.REVISIONE)));
		cliente.setTelefono(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.TELEFONO)));
		cliente.setUfficio(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.UFFICIO)));
		
		listaClienti.add(cliente);
	    }
	}
	
	if (!cursor.isClosed())
	    cursor.close();
	
	return listaClienti;
    }
    
    @Override
    protected void onPostExecute(ArrayList<Cliente> result) {
    
	Message msg = new Message();
	
	msg.what = Constants.WHAT_MESSAGE_GET_CLIENTI;
	msg.obj = (ArrayList<Cliente>) result;
	
	mHandler.sendMessage(msg);
    }
}
