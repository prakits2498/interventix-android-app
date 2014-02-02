package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListClientiAdapter;
import com.federicocolantoni.projects.interventix.controller.InterventoController;

@SuppressLint({ "InlinedApi", "NewApi" })
@EFragment(R.layout.list_customers_fragment)
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

		if (!InterventoController.controller.getIntervento().isNuovo())
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
					getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().getNumeroIntervento() + " - " + getString(R.string.row_list_clients));
		else
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_list_clients));

		mAdapter = new ListClientiAdapter(getActivity(), R.layout.list_customers_row, R.id.tv_row_nominativo);

		listClienti.setAdapter(mAdapter);
		// listClienti.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> adapter, View view, int
		// position, long id) {
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
		// if (!ListClientiAdapter.blockClick) {
		// Cliente clChecked = mAdapter.getItem(position);
		//
		// InterventoController.controller.setCliente(clChecked);
		// InterventoController.controller.getIntervento().setIdCliente(clChecked.getIdCliente());
		//
		// FragmentManager manager = getActivity().getSupportFragmentManager();
		// manager.popBackStackImmediate();
		// }
		// }
		// });
		// listClienti.setOnItemLongClickListener(new OnItemLongClickListener()
		// {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> adapter, View view, int
		// position, long id) {
		//
		// return false;
		// }
		// });

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
