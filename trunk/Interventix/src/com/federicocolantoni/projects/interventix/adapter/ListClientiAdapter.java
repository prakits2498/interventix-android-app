package com.federicocolantoni.projects.interventix.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.entity.Cliente;

public class ListClientiAdapter extends ArrayAdapter<Cliente> {
    
    private Context context;
    
    private ArrayList<Cliente> listaClienti;
    
    public ListClientiAdapter(Context context, int resource, int textViewResourceId, List<Cliente> objects) {
	
	super(context, resource, textViewResourceId, objects);
	
	this.context = context;
	listaClienti = (ArrayList<Cliente>) objects;
    }
    
    @Override
    public int getCount() {
	
	return listaClienti.size();
    }
    
    @Override
    public Cliente getItem(int position) {
	
	return listaClienti.get(position);
    }
    
    @Override
    public long getItemId(int position) {
	
	return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	
	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	if (convertView == null)
	    convertView = inflater.inflate(R.layout.client_row, null);
	
	Cliente cliente = getItem(position);
	
	TextView tv_row_nominativo = (TextView) convertView.findViewById(R.id.tv_row_nominativo);
	tv_row_nominativo.setText(String.format("%s", cliente.getmNominativo()));
	
	TextView tv_row_cod_fis = (TextView) convertView.findViewById(R.id.tv_row_cod_fis);
	tv_row_cod_fis.setText(String.format("%s", cliente.getmCodiceFiscale()));
	
	TextView tv_row_p_iva = (TextView) convertView.findViewById(R.id.tv_row_p_iva);
	tv_row_p_iva.setText(String.format("%s", cliente.getmPartitaIVA()));
	
	return convertView;
    }
}
