package com.federicocolantoni.projects.interventix.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caverock.androidsvg.SVGImageView;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.OnSwipeTouchListener;

public class ListClientiAdapter extends ArrayAdapter<Cliente> implements OnClickListener {
    
    private LayoutInflater inflater;
    
    private static boolean blockClick = false;
    
    private static SparseBooleanArray checkedItems = new SparseBooleanArray();
    private static SparseBooleanArray swipedItems = new SparseBooleanArray();
    
    private ActionBarActivity context;
    
    ViewHolder holder;
    
    public ListClientiAdapter(Context context, int resource, int textViewResourceId) {
    
	super(context, resource, textViewResourceId);
	
	this.context = (ActionBarActivity) context;
	
	for (int i = 0; i < InterventoController.listaClienti.size(); i++) {
	    checkedItems.put(i, false);
	    swipedItems.put(i, false);
	}
	
	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
    
	return InterventoController.listaClienti.size();
    }
    
    @Override
    public Cliente getItem(int position) {
    
	return InterventoController.listaClienti.get(position);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
    
	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.list_client_row, null);
	    
	    holder = new ViewHolder();
	    
	    holder.rootView = convertView.findViewById(R.id.swipe_item);
	    
	    holder.tv_row_nominativo = (TextView) convertView.findViewById(R.id.tv_row_nominativo);
	    holder.tv_row_cod_fis = (TextView) convertView.findViewById(R.id.tv_row_cod_fis);
	    holder.tv_row_p_iva = (TextView) convertView.findViewById(R.id.tv_row_p_iva);
	    holder.img_checked = (SVGImageView) convertView.findViewById(R.id.img_checked);
	    holder.modClient = (Button) convertView.findViewById(R.id.mod_cliente);
	    
	    holder.swipeButtons(position);
	    convertView.setTag(holder);
	}
	else {
	    
	    holder = (ViewHolder) convertView.getTag();
	    holder.swipeButtons(position);
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
	
	if (swipedItems.get(position)) {
	    
	    holder.modClient.setVisibility(View.VISIBLE);
	}
	else {
	    
	    holder.modClient.setVisibility(View.INVISIBLE);
	}
	
	holder.modClient.setOnClickListener(this);
	
	return convertView;
    }
    
    private class ViewHolder {
	
	public View rootView = null;
	
	TextView tv_row_nominativo;
	TextView tv_row_cod_fis;
	TextView tv_row_p_iva;
	SVGImageView img_checked;
	Button modClient;
	
	public void swipeButtons(final int position) {
	
	    rootView.setOnTouchListener(new OnSwipeTouchListener() {
		
		@Override
		public void onTouchUp() {
		
		    if (!blockClick) {
			toggleChecked(position);
			
			Cliente clChecked = getItem(position);
			
			InterventoController.controller.setCliente(clChecked);
			InterventoController.controller.getIntervento().setIdCliente(clChecked.getIdCliente());
			
			FragmentManager manager = context.getSupportFragmentManager();
			manager.popBackStackImmediate();
		    }
		}
		
		@Override
		public void onLongPressTouch() {
		
		    if (modClient.getVisibility() == View.INVISIBLE) {
			modClient.setVisibility(View.VISIBLE);
			swipedItems.put(position, true);
			blockClick = true;
			
			for (int cont = 0; cont < swipedItems.size(); cont++) {
			    
			    if (cont != position)
				swipedItems.put(cont, false);
			}
			
			for (int cont = 0; cont < checkedItems.size(); cont++) {
			    
			    if (cont != position)
				checkedItems.put(cont, false);
			}
			
			notifyDataSetChanged();
		    }
		    else {
			modClient.setVisibility(View.INVISIBLE);
			swipedItems.put(position, false);
			blockClick = false;
			
			notifyDataSetChanged();
		    }
		}
	    });
	}
    }
    
    @Override
    public void onClick(View view) {
    
	InterventixToast.makeToast("Modifica cliente", Toast.LENGTH_SHORT);
    }
}
