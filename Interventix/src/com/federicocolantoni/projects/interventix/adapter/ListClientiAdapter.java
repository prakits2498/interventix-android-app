package com.federicocolantoni.projects.interventix.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;

public class ListClientiAdapter extends CursorAdapter {
    
    private final LayoutInflater mInflater;
    private boolean mFoundIndexes;
    
    private int mNominativoClienteIndex;
    private int mCodiceFiscaleClienteIndex;
    private int mPartitaIVAClienteIndex;
    
    public ListClientiAdapter(Context context, Cursor c) {
	super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	mInflater = LayoutInflater.from(context);
	mFoundIndexes = false;
    }
    
    @Override
    public void bindView(View row, Context context, Cursor cursor) {
	
	TextView tv_row_nominativo = (TextView) row.getTag(R.id.tv_row_nominativo);
	TextView tv_row_cod_fis = (TextView) row.getTag(R.id.tv_row_cod_fis);
	TextView tv_row_piva = (TextView) row.getTag(R.id.tv_row_p_iva);
	
	if (!mFoundIndexes) {
	    
	    mNominativoClienteIndex = cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO);
	    mCodiceFiscaleClienteIndex = cursor.getColumnIndex(ClienteDB.Fields.CODICE_FISCALE);
	    mPartitaIVAClienteIndex = cursor.getColumnIndex(ClienteDB.Fields.PARTITAIVA);
	    
	    mFoundIndexes = true;
	}
	
	tv_row_nominativo.setText(cursor.getString(mNominativoClienteIndex));
	tv_row_cod_fis.setText(cursor.getString(mCodiceFiscaleClienteIndex));
	tv_row_piva.setText(cursor.getString(mPartitaIVAClienteIndex));
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	
	View view = mInflater.inflate(R.layout.client_row, parent, false);
	
	TextView tv_row_nominativo = (TextView) view.findViewById(R.id.tv_row_nominativo);
	TextView tv_row_cod_fis = (TextView) view.findViewById(R.id.tv_row_cod_fis);
	TextView tv_row_piva = (TextView) view.findViewById(R.id.tv_row_p_iva);
	
	view.setTag(R.id.tv_row_nominativo, tv_row_nominativo);
	view.setTag(R.id.tv_row_cod_fis, tv_row_cod_fis);
	view.setTag(R.id.tv_row_p_iva, tv_row_piva);
	
	return view;
    }
}
