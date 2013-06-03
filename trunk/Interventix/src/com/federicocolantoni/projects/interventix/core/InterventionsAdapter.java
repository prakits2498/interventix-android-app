
package com.federicocolantoni.projects.interventix.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.DBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.intervento.Cliente;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class InterventionsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Intervento> list;
    private Context context;

    public InterventionsAdapter(Context context, List<Intervento> list) {

	this.list = list;
	inflater = LayoutInflater.from(context);
	this.context = context;
    }

    @Override
    public int getCount() {

	return list.size();
    }

    @Override
    public Intervento getItem(int position) {

	return list.get(position);
    }

    @Override
    public long getItemId(int position) {

	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.interv_row, parent, false);
	}

	Intervento interv = getItem(position);

	TextView nomeIntervento = (TextView) convertView
		.findViewById(R.id.tv_nome);
	nomeIntervento.setText("Intervento n° " + interv.getmIdIntervento());

	TextView clienteIntervento = (TextView) convertView
		.findViewById(R.id.tv_cliente);

	GetCliente cliente = new GetCliente(context);
	cliente.execute(interv.getmIdCliente());

	try {
	    clienteIntervento.setText(cliente.get().getmNominativo() + " - ");
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}

	TextView dataIntervento = (TextView) convertView
		.findViewById(R.id.tv_data);
	dataIntervento.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY)
		.format(new Date(interv.getmDataOra())));

	return convertView;
    }

    private class GetCliente extends AsyncTask<Long, Void, Cliente> {

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

	    Cursor cursor = cr.query(ClienteDB.CONTENT_URI, null, selection,
		    null, null);

	    Cliente cliente = new Cliente();

	    if (cursor.moveToFirst()) {
		cliente.setmNominativo(cursor.getString(cursor
			.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
	    }

	    return cliente;
	}

	@Override
	protected void onPostExecute(Cliente result) {

	}
    }
}
