
package com.federicocolantoni.projects.interventix.adapter;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.DBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.intervento.Cliente;
import com.federicocolantoni.projects.interventix.utils.GetNominativoCliente;

public class InterventionsAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    private boolean mFoundIndexes;

    private int mIdInterventoIndex;
    private int mClienteInterventoIndex;
    private int mDataInterventoIndex;

    public InterventionsAdapter(Context context, Cursor c) {

	super(context, c, InterventionsAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	mInflater = LayoutInflater.from(context);
	mFoundIndexes = false;
    }

    @Override
    public void bindView(View row, Context context, Cursor cursor) {

	TextView tv_nome_intervento = (TextView) row
		.getTag(R.id.tv_nome_intervento);
	TextView tv_cliente_intervento = (TextView) row
		.getTag(R.id.tv_cliente_intervento);
	TextView tv_data_intervento = (TextView) row
		.getTag(R.id.tv_data_intervento);

	if (!mFoundIndexes) {
	    mIdInterventoIndex = cursor
		    .getColumnIndex(InterventoDB.Fields.ID_INTERVENTO);
	    mClienteInterventoIndex = cursor
		    .getColumnIndex(InterventoDB.Fields.CLIENTE);
	    mDataInterventoIndex = cursor
		    .getColumnIndex(InterventoDB.Fields.DATA_ORA);

	    mFoundIndexes = true;
	}

	Long idInterv = cursor.getLong(mIdInterventoIndex);

	Long idCliente = cursor.getLong(mClienteInterventoIndex);

	String nomeInterv = mContext.getString(R.string.numero_intervento)
		+ idInterv;

	tv_nome_intervento.setText(nomeInterv);

	GetNominativoCliente clienteAsyncTask = new GetNominativoCliente(
		mContext);
	clienteAsyncTask.execute(idCliente);

	Cliente cliente = null;
	try {
	    cliente = clienteAsyncTask.get();
	    tv_cliente_intervento.setText(cliente.getmNominativo() + " - ");
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}

	DateTime dt = new DateTime(cursor.getLong(mDataInterventoIndex));

	//	DateTimeFormatter formatter = DateTimeFormat
	//		.forPattern("dd/MM/yyyy HH:mm");
	//	formatter.withLocale(Locale.ITALY);

	tv_data_intervento.setText(dt
		.toString("dd/MM/yyyy HH:mm", Locale.ITALY));

	//	tv_data_intervento.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm",
	//		Locale.ITALY).format(new Date(cursor
	//		.getLong(mDataInterventoIndex))));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup listView) {

	View view = mInflater.inflate(R.layout.interv_row, listView, false);

	TextView tv_nome_intervento = (TextView) view
		.findViewById(R.id.tv_nome_intervento);
	TextView tv_cliente_intervento = (TextView) view
		.findViewById(R.id.tv_cliente_intervento);
	TextView tv_data_intervento = (TextView) view
		.findViewById(R.id.tv_data_intervento);

	view.setTag(R.id.tv_nome_intervento, tv_nome_intervento);
	view.setTag(R.id.tv_cliente_intervento, tv_cliente_intervento);
	view.setTag(R.id.tv_data_intervento, tv_data_intervento);

	return view;
    }
}
