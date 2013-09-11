package com.federicocolantoni.projects.interventix.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.intervento.Cliente;

public class GetNominativoClienteAsyncTask extends AsyncTask<Long, Void, Cliente> {
    
    private Context context;
    
    public GetNominativoClienteAsyncTask(Context context) {
	
	this.context = context.getApplicationContext();
    }
    
    @Override
    protected Cliente doInBackground(Long... params) {
	
	ContentResolver cr = context.getContentResolver();
	
	String[] projection = new String[] {
		ClienteDB.Fields._ID, ClienteDB.Fields.NOMINATIVO
	};
	
	String selection = ClienteDB.Fields.TYPE + " =? AND " + ClienteDB.Fields.ID_CLIENTE + " = ?";
	
	String[] selectionArgs = new String[] {
		ClienteDB.CLIENTE_ITEM_TYPE, "" + params[0]
	};
	
	Cursor cursor = cr.query(ClienteDB.CONTENT_URI, projection, selection, selectionArgs, null);
	
	Cliente cliente = new Cliente();
	
	if (cursor.moveToFirst()) {
	    cliente.setmNominativo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
	}
	
	if (!cursor.isClosed()) {
	    cursor.close();
	}
	else {
	    System.out.println("Cursor for " + this.getClass().getSimpleName() + " is closed");
	}
	
	return cliente;
    }
    
    @Override
    protected void onPostExecute(Cliente result) {
	
    }
}
