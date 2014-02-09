package com.federicocolantoni.projects.interventix.adapter;

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
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.task.GetNominativoClienteAsyncTask;

public class ListInterventiAdapter extends BaseAdapter {
    
    private final LayoutInflater mInflater;
    private List<Intervento> listaInterventi;
    
    // private boolean mFoundIndexes;
    //
    // private int mNumeroInterventoIndex;
    // private int mClienteInterventoIndex;
    // private int mDataInterventoIndex;
    // private int mConflittoInterventoIndex;
    // private int mModificatoInterventoIndex;
    // private int mNuovoInterventoIndex;
    
    private static SparseBooleanArray modifiedAndNewInterventions = new SparseBooleanArray();
    
    public ListInterventiAdapter(List<Intervento> listaInterventiAperti) {
    
	mInflater = LayoutInflater.from(com.federicocolantoni.projects.interventix.Interventix_.getContext());
	listaInterventi = listaInterventiAperti;
	
	// mFoundIndexes = false;
    }
    
    // @Override
    // public void bindView(View row, Context context, Cursor cursor) {
    //
    // TextView tv_numero_intervento = (TextView)
    // row.getTag(R.id.tv_numero_intervento);
    // TextView tv_cliente_intervento = (TextView)
    // row.getTag(R.id.tv_cliente_intervento);
    // TextView tv_data_intervento = (TextView)
    // row.getTag(R.id.tv_data_intervento);
    //
    // if (!mFoundIndexes) {
    // mNumeroInterventoIndex =
    // cursor.getColumnIndex(InterventoDB.Fields.NUMERO_INTERVENTO);
    // mClienteInterventoIndex =
    // cursor.getColumnIndex(InterventoDB.Fields.CLIENTE);
    // mDataInterventoIndex =
    // cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA);
    // mConflittoInterventoIndex =
    // cursor.getColumnIndex(InterventoDB.Fields.CONFLITTO);
    // mModificatoInterventoIndex =
    // cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO);
    // mNuovoInterventoIndex = cursor.getColumnIndex(InterventoDB.Fields.NUOVO);
    //
    // mFoundIndexes = true;
    // }
    //
    // boolean conflitto = cursor.getInt(mConflittoInterventoIndex) == 1 ? true
    // : false;
    //
    // if (conflitto)
    // row.setBackgroundColor(Color.RED);
    //
    // String modificato = cursor.getString(mModificatoInterventoIndex);
    // String nuovo = cursor.getString(mNuovoInterventoIndex);
    //
    // if (modificato.equals(Constants.INTERVENTO_MODIFICATO) ||
    // modificato.equals(Constants.INTERVENTO_NUOVO))
    // modifiedAndNewInterventions.put(cursor.getPosition(), true);
    // else
    // modifiedAndNewInterventions.put(cursor.getPosition(), false);
    //
    // if (modifiedAndNewInterventions.get(cursor.getPosition()))
    // row.setBackgroundResource(R.drawable.list_pressed_modified_item);
    // else
    // row.setBackgroundResource(R.drawable.list_pressed_item);
    //
    // Long idInterv = cursor.getLong(mNumeroInterventoIndex);
    //
    // Long idCliente = cursor.getLong(mClienteInterventoIndex);
    //
    // String numeroInterv = context.getString(R.string.numero_intervento) +
    // idInterv;
    // tv_numero_intervento.setText(numeroInterv);
    //
    // if (nuovo != null)
    // if (nuovo.equals(Constants.INTERVENTO_NUOVO)) {
    //
    // String oldText = tv_numero_intervento.getText().toString();
    // tv_numero_intervento.setText(oldText.concat(" (N)"));
    // }
    //
    // GetNominativoClienteAsyncTask clienteAsyncTask = new
    // GetNominativoClienteAsyncTask(context);
    // clienteAsyncTask.execute(idCliente);
    //
    // Cliente cliente = null;
    // try {
    // cliente = clienteAsyncTask.get();
    // tv_cliente_intervento.setText(cliente.nominativo);
    // }
    // catch (InterruptedException e) {
    //
    // BugSenseHandler.sendException(e);
    //
    // e.printStackTrace();
    // }
    // catch (ExecutionException e) {
    //
    // BugSenseHandler.sendException(e);
    //
    // e.printStackTrace();
    // }
    //
    // DateTime dt = new DateTime(cursor.getLong(mDataInterventoIndex));
    //
    // tv_data_intervento.setText(dt.toString(context.getString(R.string.date_format),
    // Locale.ITALY) + " - ");
    // }
    
    // @Override
    // public View newView(Context context, Cursor cursor, ViewGroup listView) {
    //
    // View view = mInflater.inflate(R.layout.list_interventions_row, listView,
    // false);
    //
    // TextView tv_numero_intervento = (TextView)
    // view.findViewById(R.id.tv_numero_intervento);
    // TextView tv_cliente_intervento = (TextView)
    // view.findViewById(R.id.tv_cliente_intervento);
    // TextView tv_data_intervento = (TextView)
    // view.findViewById(R.id.tv_data_intervento);
    //
    // view.setTag(R.id.tv_numero_intervento, tv_numero_intervento);
    // view.setTag(R.id.tv_cliente_intervento, tv_cliente_intervento);
    // view.setTag(R.id.tv_data_intervento, tv_data_intervento);
    //
    // return view;
    // }
    
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
	
	String numeroInterv = com.federicocolantoni.projects.interventix.Interventix_.getContext().getString(R.string.numero_intervento) + intervento.numero;
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
	
	tvDataIntervento.setText(dt.toString(com.federicocolantoni.projects.interventix.Interventix_.getContext().getString(R.string.date_format), Locale.ITALY) + " - ");
	
	return convertView;
    }
}
