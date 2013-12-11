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

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.task.GetNominativoClienteAsyncTask;

public class ListInterventiAdapter extends CursorAdapter {
    
    private final LayoutInflater mInflater;
    private boolean mFoundIndexes;
    
    private int mNumeroInterventoIndex;
    private int mClienteInterventoIndex;
    private int mDataInterventoIndex;
    
    public ListInterventiAdapter(Context context, Cursor c) {
	
	super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	mInflater = LayoutInflater.from(context);
	mFoundIndexes = false;
    }
    
    @Override
    public void bindView(View row, Context context, Cursor cursor) {
	
	TextView tv_numero_intervento = (TextView) row.getTag(R.id.tv_numero_intervento);
	TextView tv_cliente_intervento = (TextView) row.getTag(R.id.tv_cliente_intervento);
	TextView tv_data_intervento = (TextView) row.getTag(R.id.tv_data_intervento);
	
	if (!mFoundIndexes) {
	    mNumeroInterventoIndex = cursor.getColumnIndex(InterventoDB.Fields.NUMERO_INTERVENTO);
	    mClienteInterventoIndex = cursor.getColumnIndex(InterventoDB.Fields.CLIENTE);
	    mDataInterventoIndex = cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA);
	    
	    mFoundIndexes = true;
	}
	
	Long idInterv = cursor.getLong(mNumeroInterventoIndex);
	
	Long idCliente = cursor.getLong(mClienteInterventoIndex);
	
	String numeroInterv = mContext.getString(R.string.numero_intervento) + idInterv;
	
	tv_numero_intervento.setText(numeroInterv);
	
	GetNominativoClienteAsyncTask clienteAsyncTask = new GetNominativoClienteAsyncTask(mContext);
	clienteAsyncTask.execute(idCliente);
	
	Cliente cliente = null;
	try {
	    cliente = clienteAsyncTask.get();
	    tv_cliente_intervento.setText(cliente.getmNominativo() + " - ");
	}
	catch (InterruptedException e) {
	    
	    BugSenseHandler.sendException(e);
	    
	    e.printStackTrace();
	}
	catch (ExecutionException e) {
	    
	    BugSenseHandler.sendException(e);
	    
	    e.printStackTrace();
	}
	
	DateTime dt = new DateTime(cursor.getLong(mDataInterventoIndex));
	
	tv_data_intervento.setText(dt.toString(mContext.getString(R.string.date_format), Locale.ITALY));
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup listView) {
	
	View view = mInflater.inflate(R.layout.interv_row, listView, false);
	
	TextView tv_numero_intervento = (TextView) view.findViewById(R.id.tv_numero_intervento);
	TextView tv_cliente_intervento = (TextView) view.findViewById(R.id.tv_cliente_intervento);
	TextView tv_data_intervento = (TextView) view.findViewById(R.id.tv_data_intervento);
	
	view.setTag(R.id.tv_numero_intervento, tv_numero_intervento);
	view.setTag(R.id.tv_cliente_intervento, tv_cliente_intervento);
	view.setTag(R.id.tv_data_intervento, tv_data_intervento);
	
	return view;
    }
}
