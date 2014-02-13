package com.federicocolantoni.projects.interventix.ui.fragments;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListClientiAdapter;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.entity.Cliente;

@SuppressLint({ "InlinedApi", "NewApi" })
@EFragment(R.layout.fragment_list_customers)
public class ListClientsInterventoFragment extends Fragment {

	@ViewById(R.id.search_client)
	EditText searchClient;

	@ViewById(R.id.list_clients)
	ListView listClienti;

	private ListClientiAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
		((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setHasOptionsMenu(true);
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {

		super.onStart();

		if (!InterventoController.controller.getIntervento().nuovo)
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
					getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.row_list_clients));
		else
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_list_clients));

		mAdapter = new ListClientiAdapter(getActivity(), R.layout.list_customers_row, R.id.tv_row_nominativo);

		listClienti.setAdapter(mAdapter);

		listClienti.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

				if (!ListClientiAdapter.blockClick) {
					mAdapter.toggleChecked(position);

					Cliente clChecked = mAdapter.getItem(position);

					InterventoController.controller.setCliente(clChecked);
					InterventoController.controller.getIntervento().cliente = (clChecked.idcliente);

					FragmentManager manager = getActivity().getSupportFragmentManager();
					manager.popBackStackImmediate();
				}
			}
		});
		listClienti.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {

				Button modClient = (Button) view.findViewById(R.id.mod_cliente);

				if (modClient.getVisibility() == View.INVISIBLE) {
					modClient.setVisibility(View.VISIBLE);
					ListClientiAdapter.swipedItems.put(position, true);
					ListClientiAdapter.blockClick = true;

					for (int cont = 0; cont < ListClientiAdapter.swipedItems.size(); cont++) {

						if (cont != position)
							ListClientiAdapter.swipedItems.put(cont, false);
					}

					for (int cont = 0; cont < ListClientiAdapter.checkedItems.size(); cont++) {

						if (cont != position)
							ListClientiAdapter.checkedItems.put(cont, false);
					}

					mAdapter.notifyDataSetChanged();
				} else {
					modClient.setVisibility(View.INVISIBLE);
					ListClientiAdapter.swipedItems.put(position, false);
					ListClientiAdapter.blockClick = false;

					mAdapter.notifyDataSetChanged();
				}

				return true;
			}
		});

		searchClient.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				mAdapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	// @Override
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	//
	// inflater.inflate(R.menu.menu_clients, menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// switch (item.getItemId()) {
	//
	// case R.id.menu_save_client:
	//
	// int cliente_checked = 0;
	//
	// SparseBooleanArray sparseArray = mAdapter.getBooleanArray();
	//
	// for (int i = 0; i < sparseArray.size(); i++) {
	//
	// if (sparseArray.get(i)) {
	// cliente_checked = i;
	// break;
	// }
	// }
	//
	// Cliente clChecked = mAdapter.getItem(cliente_checked);
	//
	// InterventoController.controller.setCliente(clChecked);
	// InterventoController.controller.getIntervento().setIdCliente(clChecked.getIdCliente());
	//
	// break;
	// }
	//
	// return getActivity().getSupportFragmentManager().popBackStackImmediate();
	// }
}
