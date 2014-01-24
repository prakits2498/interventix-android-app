package com.federicocolantoni.projects.interventix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;

public class ListDettagliInterventiAdapter extends BaseAdapter {
    
    private final LayoutInflater mInflater;
    
    public ListDettagliInterventiAdapter(Context context) {
    
	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
    
	return InterventoController.controller.getListaDettagli().size();
    }
    
    @Override
    public DettaglioIntervento getItem(int position) {
    
	return InterventoController.controller.getListaDettagli().get(position);
    }
    
    @Override
    public long getItemId(int position) {
    
	return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    
	if (convertView == null) {
	    
	    convertView = mInflater.inflate(R.layout.list_details_interv_row, parent, false);
	}
	
	DettaglioIntervento dettaglio = getItem(position);
	
	TextView tv_type_detail_interv = (TextView) convertView.findViewById(R.id.tv_type_detail_interv);
	TextView tv_object_detail_interv = (TextView) convertView.findViewById(R.id.tv_object_detail_interv);
	
	tv_type_detail_interv.setText(dettaglio.getTipo());
	tv_object_detail_interv.setText(dettaglio.getOggetto());
	
	return convertView;
    }
}
