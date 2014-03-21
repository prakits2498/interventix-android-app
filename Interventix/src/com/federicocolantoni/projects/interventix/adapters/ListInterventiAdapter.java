package com.federicocolantoni.projects.interventix.adapters;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.models.Cliente;
import com.federicocolantoni.projects.interventix.models.Intervento;
import com.federicocolantoni.projects.interventix.task.GetNominativoClienteAsyncTask;

public class ListInterventiAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private List<Intervento> listaInterventi;

    private static SparseBooleanArray modifiedAndNewInterventions = new SparseBooleanArray();

    public ListInterventiAdapter(List<Intervento> listaInterventiAperti) {

	mInflater = LayoutInflater.from(com.federicocolantoni.projects.interventix.application.Interventix_.getContext());
	listaInterventi = listaInterventiAperti;
    }

    @Override
    public int getCount() {

	return listaInterventi.size();
    }

    @Override
    public Intervento getItem(int position) {

	return listaInterventi.get(position);
    }

    @Override
    public long getItemId(int position) {

	return listaInterventi.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	if (convertView == null)
	    convertView = mInflater.inflate(R.layout.list_interventions_row, parent, false);

	TextView tvNumeroIntervento = (TextView) convertView.findViewById(R.id.tv_numero_intervento);
	TextView tvClienteIntervento = (TextView) convertView.findViewById(R.id.tv_cliente_intervento);
	TextView tvDataIntervento = (TextView) convertView.findViewById(R.id.tv_data_intervento);

	Intervento intervento = getItem(position);

	if (intervento.conflitto)
	    convertView.setBackgroundColor(Color.RED);

	String modificato = intervento.modificato;

	if (modificato.equals(Constants.INTERVENTO_MODIFICATO) || modificato.equals(Constants.INTERVENTO_NUOVO))
	    modifiedAndNewInterventions.put(position, true);
	else
	    modifiedAndNewInterventions.put(position, false);

	if (modifiedAndNewInterventions.get(position))
	    convertView.setBackgroundResource(R.drawable.list_pressed_modified_item);
	else
	    convertView.setBackgroundResource(R.drawable.list_pressed_item);

	String numeroInterv = com.federicocolantoni.projects.interventix.application.Interventix_.getContext().getString(R.string.numero_intervento) + intervento.numero;
	tvNumeroIntervento.setText(numeroInterv);

	if (intervento.nuovo) {

	    String oldText = tvNumeroIntervento.getText().toString();
	    tvNumeroIntervento.setText(oldText.concat(" (N)"));
	}

	GetNominativoClienteAsyncTask clienteAsyncTask = new GetNominativoClienteAsyncTask();
	clienteAsyncTask.execute(intervento.cliente);

	Cliente cliente = null;

	try {

	    cliente = clienteAsyncTask.get();
	    tvClienteIntervento.setText(cliente.nominativo);
	}
	catch (InterruptedException e) {

	    BugSenseHandler.sendException(e);

	    e.printStackTrace();
	}
	catch (ExecutionException e) {

	    BugSenseHandler.sendException(e);

	    e.printStackTrace();
	}

	DateTime dt = new DateTime(intervento.dataora);

	tvDataIntervento.setText(dt.toString(com.federicocolantoni.projects.interventix.application.Interventix_.getContext().getString(R.string.date_format), Locale.ITALY) + " - ");

	return convertView;
    }
}
