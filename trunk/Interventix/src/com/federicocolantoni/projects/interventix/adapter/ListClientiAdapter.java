package com.federicocolantoni.projects.interventix.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.caverock.androidsvg.SVGImageView;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.entity.Cliente;

public class ListClientiAdapter extends ArrayAdapter<Cliente> {
    
    private ArrayList<Cliente> listaClienti;
    
    private LayoutInflater inflater;
    
    private static SparseBooleanArray checkedItems = new SparseBooleanArray();
    
    public ListClientiAdapter(Context context, int resource, int textViewResourceId, List<Cliente> objects) {
	
	super(context, resource, textViewResourceId, objects);
	
	listaClienti = (ArrayList<Cliente>) objects;
	
	for (int i = 0; i < objects.size(); i++) {
	    checkedItems.put(i, false);
	}
	
	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    
    public void toggleChecked(int position) {
	
	if (checkedItems.get(position)) {
	    checkedItems.put(position, false);
	}
	else {
	    checkedItems.put(position, true);
	}
	
	for (int i = 0; i < checkedItems.size(); i++) {
	    if (i != position)
		checkedItems.put(i, false);
	}
	
	notifyDataSetChanged();
    }
    
    public List<Integer> getCheckedItemPositions() {
	List<Integer> checkedItemPositions = new ArrayList<Integer>();
	
	for (int i = 0; i < checkedItems.size(); i++) {
	    if (checkedItems.get(i)) {
		checkedItemPositions.add(i);
	    }
	}
	
	return checkedItemPositions;
    }
    
    public List<String> getCheckedItems() {
	List<String> checkedItems = new ArrayList<String>();
	
	for (int i = 0; i < ListClientiAdapter.checkedItems.size(); i++) {
	    if (ListClientiAdapter.checkedItems.get(i)) {
		checkedItems.add(getItem(i).getNominativo());
	    }
	}
	
	return checkedItems;
    }
    
    public SparseBooleanArray getBooleanArray() {
	
	return checkedItems;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	
	ViewHolder holder;
	
	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.client_row, null);
	    
	    holder = new ViewHolder();
	    
	    holder.tv_row_nominativo = (TextView) convertView.findViewById(R.id.tv_row_nominativo);
	    holder.tv_row_cod_fis = (TextView) convertView.findViewById(R.id.tv_row_cod_fis);
	    holder.tv_row_p_iva = (TextView) convertView.findViewById(R.id.tv_row_p_iva);
	    holder.img_checked = (SVGImageView) convertView.findViewById(R.id.img_checked);
	    
	    convertView.setTag(holder);
	}
	else {
	    
	    holder = (ViewHolder) convertView.getTag();
	}
	
	Cliente cliente = getItem(position);
	
	holder.tv_row_nominativo.setText(String.format("%s", cliente.getNominativo()));
	holder.tv_row_cod_fis.setText(String.format("%s", cliente.getCodiceFiscale()));
	holder.tv_row_p_iva.setText(String.format("%s", cliente.getPartitaIVA()));
	
	if (checkedItems.get(position)) {
	    
	    holder.tv_row_nominativo.setTypeface(null, Typeface.BOLD);
	    holder.tv_row_cod_fis.setTypeface(null, Typeface.BOLD);
	    holder.tv_row_p_iva.setTypeface(null, Typeface.BOLD);
	    holder.img_checked.setVisibility(View.VISIBLE);
	}
	else {
	    holder.tv_row_nominativo.setTypeface(null, Typeface.NORMAL);
	    holder.tv_row_cod_fis.setTypeface(null, Typeface.NORMAL);
	    holder.tv_row_p_iva.setTypeface(null, Typeface.NORMAL);
	    holder.img_checked.setVisibility(View.INVISIBLE);
	}
	
	return convertView;
    }
    
    static class ViewHolder {
	TextView tv_row_nominativo;
	TextView tv_row_cod_fis;
	TextView tv_row_p_iva;
	SVGImageView img_checked;
    }
}
