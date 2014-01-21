package com.federicocolantoni.projects.interventix.fragments;

import java.util.ArrayList;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListClientiAdapter;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.task.GetClientiAsyncTask;

@SuppressLint({ "InlinedApi", "NewApi" })
@EFragment(R.layout.list_clients_fragment)
@SuppressWarnings("unchecked")
public class ListClientsInterventoFragment extends Fragment {

	@ViewById(R.id.list_clients)
	ListView listClienti;

	private ListClientiAdapter mAdapter;

	Handler handler = new MyHandler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {

				case Constants.WHAT_MESSAGE_GET_CLIENTI:

					mAdapter = new ListClientiAdapter(getActivity(), R.layout.list_client_row, R.id.tv_row_nominativo, (ArrayList<Cliente>) msg.obj);

					listClienti.setAdapter(mAdapter);

					break;
			}
		};
	};

	private static class MyHandler extends Handler {

	}

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

		new GetClientiAsyncTask(getActivity(), handler).execute();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.menu_clients, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.menu_save_client:

				int cliente_checked = 0;

				SparseBooleanArray sparseArray = mAdapter.getBooleanArray();

				for (int i = 0; i < sparseArray.size(); i++) {

					if (sparseArray.get(i))
						cliente_checked = i;
				}

				Cliente clChecked = mAdapter.getItem(cliente_checked);

				InterventoController.controller.setCliente(clChecked);
				InterventoController.controller.getIntervento().setIdCliente(clChecked.getIdCliente());

				break;
		}

		return getActivity().getSupportFragmentManager().popBackStackImmediate();
	}
}
