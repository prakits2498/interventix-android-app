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

		String[] PROJECTION = new String[] { ClienteDB.Fields._ID, ClienteDB.Fields.NOMINATIVO, ClienteDB.Fields.CODICE_FISCALE, ClienteDB.Fields.PARTITAIVA,
				ClienteDB.Fields.ID_CLIENTE };

		String SELECTION = ClienteDB.Fields.TYPE + "=?";

		String[] SELECTION_ARGS = new String[] { ClienteDB.CLIENTE_ITEM_TYPE };

		String sortOrder = ClienteDB.Fields.NOMINATIVO + " ASC";

		ContentResolver cr = mContext.getContentResolver();

		Cursor cursor = cr.query(ClienteDB.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, sortOrder);

		if (cursor.moveToFirst()) {

			while (cursor.moveToNext()) {

				Cliente cliente = new Cliente(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.ID_CLIENTE)));

				cliente.setNominativo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
				cliente.setCodiceFiscale(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CODICE_FISCALE)));
				cliente.setPartitaIVA(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.PARTITAIVA)));

				listaClienti.add(cliente);
			}
		}

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
