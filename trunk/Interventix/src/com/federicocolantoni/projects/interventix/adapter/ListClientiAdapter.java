package com.federicocolantoni.projects.interventix.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.entity.Cliente;

public class ListClientiAdapter extends ArrayAdapter<Cliente> implements OnClickListener, Filterable {

	private LayoutInflater inflater;

	public static boolean blockClick = false;

	public static SparseBooleanArray clickedItems = new SparseBooleanArray();
	public static SparseBooleanArray checkedItems = new SparseBooleanArray();
	public static SparseBooleanArray swipedItems = new SparseBooleanArray();

	// private ActionBarActivity context;

	ViewHolder holder;

	List<Cliente> listaClientiTemp = InterventoController.listaClienti;
	private ClienteFilter filter;

	public ListClientiAdapter(Context context, int resource, int textViewResourceId) {

		super(context, resource, textViewResourceId);

		// this.context = (ActionBarActivity) context;

		for (int i = 0; i < InterventoController.listaClienti.size(); i++) {
			checkedItems.put(i, false);
			swipedItems.put(i, false);
			clickedItems.put(i, true);
		}

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return listaClientiTemp.size();
	}

	@Override
	public Cliente getItem(int position) {

		return listaClientiTemp.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	public void toggleChecked(int position) {

		if (checkedItems.get(position)) {
			checkedItems.put(position, false);
		} else {
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
				checkedItems.add(getItem(i).nominativo);
			}
		}

		return checkedItems;
	}

	public SparseBooleanArray getCheckedArray() {

		return checkedItems;
	}

	@Override
	public boolean isEnabled(int position) {

		if (clickedItems.get(position))
			return true;
		else {
			return false;
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_customers_row, null);

			holder = new ViewHolder();

			// holder.rootView = convertView.findViewById(R.id.swipe_item);

			holder.tvRowNominativo = (TextView) convertView.findViewById(R.id.tv_row_nominativo);
			holder.tvRowCodFis = (TextView) convertView.findViewById(R.id.tv_row_cod_fis);
			holder.tvRowPIva = (TextView) convertView.findViewById(R.id.tv_row_p_iva);
			holder.modClient = (Button) convertView.findViewById(R.id.mod_cliente);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		Cliente cliente = getItem(position);

		holder.tvRowNominativo.setText(String.format("%s", cliente.nominativo));
		holder.tvRowCodFis.setText(String.format("%s", cliente.codicefiscale));
		holder.tvRowPIva.setText(String.format("%s", cliente.partitaiva));

		if (checkedItems.get(position)) {

			holder.tvRowNominativo.setTypeface(null, Typeface.BOLD);
			holder.tvRowCodFis.setTypeface(null, Typeface.BOLD);
			holder.tvRowPIva.setTypeface(null, Typeface.BOLD);
		} else {

			holder.tvRowNominativo.setTypeface(null, Typeface.NORMAL);
			holder.tvRowCodFis.setTypeface(null, Typeface.NORMAL);
			holder.tvRowPIva.setTypeface(null, Typeface.NORMAL);
		}

		if (swipedItems.get(position)) {

			holder.modClient.setVisibility(View.VISIBLE);
		} else {

			holder.modClient.setVisibility(View.INVISIBLE);
		}

		holder.modClient.setOnClickListener(this);

		return convertView;
	}

	private class ViewHolder {

		// public View rootView = null;

		TextView tvRowNominativo;
		TextView tvRowCodFis;
		TextView tvRowPIva;
		Button modClient;

		// public void swipeButtons(final int position) {
		//
		// rootView.setOnTouchListener(new OnSwipeTouchListener() {
		//
		// @Override
		// public void onTouchUp() {
		//
		// if (!blockClick) {
		// toggleChecked(position);
		//
		// Cliente clChecked = getItem(position);
		//
		// InterventoController.controller.setCliente(clChecked);
		// InterventoController.controller.getIntervento().cliente =
		// (clChecked.idcliente);
		//
		// FragmentManager manager = context.getSupportFragmentManager();
		// manager.popBackStackImmediate();
		// }
		// }
		//
		// @Override
		// public void onLongPressTouch() {
		//
		// if (modClient.getVisibility() == View.INVISIBLE) {
		// modClient.setVisibility(View.VISIBLE);
		// swipedItems.put(position, true);
		// blockClick = true;
		//
		// for (int cont = 0; cont < swipedItems.size(); cont++) {
		//
		// if (cont != position)
		// swipedItems.put(cont, false);
		// }
		//
		// for (int cont = 0; cont < checkedItems.size(); cont++) {
		//
		// if (cont != position)
		// checkedItems.put(cont, false);
		// }
		//
		// notifyDataSetChanged();
		// } else {
		// modClient.setVisibility(View.INVISIBLE);
		// swipedItems.put(position, false);
		// blockClick = false;
		//
		// notifyDataSetChanged();
		// }
		// }
		// });
		// }
	}

	@Override
	public Filter getFilter() {

		if (filter == null)
			filter = new ClienteFilter();

		return filter;
	}

	private class ClienteFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			FilterResults result = new FilterResults();

			if (constraint == null || constraint.length() == 0) {

				listaClientiTemp = InterventoController.listaClienti;
				result.values = listaClientiTemp;
				result.count = listaClientiTemp.size();
			} else {

				listaClientiTemp = InterventoController.listaClienti;
				List<Cliente> listClientiFiltered = new ArrayList<Cliente>();

				for (Cliente cl : listaClientiTemp) {

					if (cl.nominativo.toUpperCase(Locale.ITALIAN).contains(constraint.toString().toUpperCase(Locale.ITALIAN)))
						listClientiFiltered.add(cl);
				}

				result.count = listClientiFiltered.size();
				result.values = listClientiFiltered;
			}

			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {

			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				listaClientiTemp = (List<Cliente>) results.values;
				notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onClick(View view) {

		// InterventixToast.makeToast("Modifica cliente", Toast.LENGTH_SHORT);
	}
}
