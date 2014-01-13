package com.federicocolantoni.projects.interventix.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;

public class ListDettagliInterventiAdapter extends CursorAdapter {
    
    private final LayoutInflater mInflater;
    private boolean mFoundIndexes;
    
    private int mTipoDettaglioInterventoIndex;
    private int mOggettoDettaglioInterventoIndex;
    
    public ListDettagliInterventiAdapter(Context context, Cursor c) {
    
	super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	mInflater = LayoutInflater.from(context);
	mFoundIndexes = false;
    }
    
    @Override
    public void bindView(View row, Context context, Cursor cursor) {
    
	TextView tv_type_detail_interv = (TextView) row.getTag(R.id.tv_type_detail_interv);
	TextView tv_object_detail_interv = (TextView) row.getTag(R.id.tv_object_detail_interv);
	
	if (!mFoundIndexes) {
	    
	    mTipoDettaglioInterventoIndex = cursor.getColumnIndex(DettaglioInterventoDB.Fields.TIPO);
	    mOggettoDettaglioInterventoIndex = cursor.getColumnIndex(DettaglioInterventoDB.Fields.OGGETTO);
	    
	    mFoundIndexes = true;
	}
	
	String tipoDettInterv = cursor.getString(mTipoDettaglioInterventoIndex);
	String oggettoDettInterv = cursor.getString(mOggettoDettaglioInterventoIndex);
	
	tv_type_detail_interv.setText(tipoDettInterv);
	tv_object_detail_interv.setText(oggettoDettInterv);
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
    
	View view = mInflater.inflate(R.layout.list_details_interv_row, parent, false);
	
	TextView tv_type_detail_interv = (TextView) view.findViewById(R.id.tv_type_detail_interv);
	TextView tv_object_detail_interv = (TextView) view.findViewById(R.id.tv_object_detail_interv);
	
	view.setTag(R.id.tv_type_detail_interv, tv_type_detail_interv);
	view.setTag(R.id.tv_object_detail_interv, tv_object_detail_interv);
	
	return view;
    }
}
