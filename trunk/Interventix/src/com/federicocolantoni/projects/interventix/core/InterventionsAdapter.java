
package com.federicocolantoni.projects.interventix.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Cliente;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.utils.GetCliente;

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

	RowWrapper rowWrapper;
	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.interv_row, parent, false);
	    rowWrapper = new RowWrapper(convertView);
	    convertView.setTag(rowWrapper);
	} else {
	    rowWrapper = (RowWrapper) convertView.getTag();
	}

	Intervento interv = getItem(position);

	GetCliente cliente = new GetCliente(context);
	cliente.execute(interv.getmIdCliente());

	try {
	    rowWrapper.populateRow(interv, cliente.get());
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}

	return convertView;
    }

    private static class RowWrapper {

	private TextView nomeIntervento;
	private TextView clienteIntervento;
	private TextView dataIntervento;

	public RowWrapper(View convertView) {

	    nomeIntervento = (TextView) convertView.findViewById(R.id.tv_nome);
	    clienteIntervento = (TextView) convertView
		    .findViewById(R.id.tv_cliente);
	    dataIntervento = (TextView) convertView.findViewById(R.id.tv_data);
	}

	public void populateRow(Intervento interv, Cliente cliente) {

	    nomeIntervento
		    .setText("Intervento n° " + interv.getmIdIntervento());
	    clienteIntervento.setText(cliente.getmNominativo() + " - ");
	    dataIntervento.setText(new SimpleDateFormat("dd/MM/yyyy",
		    Locale.ITALY).format(new Date(interv.getmDataOra())));
	}
    }
}
