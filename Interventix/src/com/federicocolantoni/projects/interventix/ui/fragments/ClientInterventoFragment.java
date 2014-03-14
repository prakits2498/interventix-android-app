package com.federicocolantoni.projects.interventix.ui.fragments;

import org.androidannotations.annotations.EFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.j256.ormlite.dao.RuntimeExceptionDao;

@EFragment(R.layout.fragment_customer)
public class ClientInterventoFragment extends Fragment {

    private Cliente cliente;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {

	super.onStart();

	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
		    getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.row_client));
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_client));

	Bundle args = getArguments();

	RuntimeExceptionDao<Cliente, Long> clienteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeClienteDao();

	cliente = clienteDao.queryForId(args.getLong(Constants.CLIENTE));

	com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	super.onCreateOptionsMenu(menu, inflater);

	inflater.inflate(R.menu.menu_clients, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {

	super.onResume();

	updateUI();
    }

    private void updateUI() {

    }
}
