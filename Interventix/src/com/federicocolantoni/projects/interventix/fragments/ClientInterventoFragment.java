package com.federicocolantoni.projects.interventix.fragments;

import java.util.ArrayList;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.models.Cliente;
import com.federicocolantoni.projects.interventix.models.InterventoController;
import com.j256.ormlite.dao.RuntimeExceptionDao;

@EFragment(R.layout.fragment_customer)
public class ClientInterventoFragment extends Fragment {

    private Cliente cliente;

    @ViewById(R.id.tv_nomin_client)
    EditText editNominativoCliente;

    @ViewById(R.id.edit_codfis_client)
    EditText editCodFisCliente;

    @ViewById(R.id.edit_partitaiva_client)
    EditText editPartitaIvaCliente;

    @ViewById(R.id.edit_telefono_client)
    EditText editTelefonoCliente;

    @ViewById(R.id.edit_fax_client)
    EditText editFaxCliente;

    @ViewById(R.id.edit_email_client)
    EditText editEmailCliente;

    @ViewById(R.id.edit_ufficio_client)
    EditText editUfficioCliente;

    @ViewById(R.id.edit_referente_client)
    EditText editReferenteCliente;

    @ViewById(R.id.edit_citta_client)
    EditText editCittaCliente;

    @ViewById(R.id.edit_indirizzo_client)
    EditText editIndirizzoCliente;

    @ViewById(R.id.edit_interno_client)
    EditText editInternoCliente;

    @ViewById(R.id.edit_note_client)
    EditText editNoteCliente;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Cliente.class)
    RuntimeExceptionDao<Cliente, Long> clienteDao;

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

	cliente = clienteDao.queryForId(args.getLong(Constants.CLIENTE));

	editCittaCliente.setText(cliente.citta);
	editCodFisCliente.setText(cliente.codicefiscale);
	editEmailCliente.setText(cliente.email);
	editFaxCliente.setText(cliente.fax);
	editIndirizzoCliente.setText(cliente.indirizzo);
	editInternoCliente.setText(cliente.interno);
	editNominativoCliente.setText(cliente.nominativo);
	editNoteCliente.setText(cliente.note);
	editPartitaIvaCliente.setText(cliente.partitaiva);
	editReferenteCliente.setText(cliente.referente);
	editTelefonoCliente.setText(cliente.telefono);
	editUfficioCliente.setText(cliente.ufficio);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	super.onCreateOptionsMenu(menu, inflater);

	inflater.inflate(R.menu.menu_clients, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {
	    case R.id.menu_save_client:

		cliente.citta = editCittaCliente.getText().toString();
		cliente.codicefiscale = editCodFisCliente.getText().toString();
		cliente.email = editEmailCliente.getText().toString();
		cliente.fax = editFaxCliente.getText().toString();
		cliente.indirizzo = editIndirizzoCliente.getText().toString();
		cliente.interno = editInternoCliente.getText().toString();
		cliente.nominativo = editNominativoCliente.getText().toString();
		cliente.note = editNoteCliente.getText().toString();
		cliente.partitaiva = editPartitaIvaCliente.getText().toString();
		cliente.referente = editReferenteCliente.getText().toString();
		cliente.telefono = editTelefonoCliente.getText().toString();
		cliente.ufficio = editUfficioCliente.getText().toString();

		clienteDao.update(cliente);
		InterventoController.listaClienti = (ArrayList<Cliente>) clienteDao.queryForAll();

		FragmentManager manager = getActivity().getSupportFragmentManager();

		manager.popBackStackImmediate();

		break;
	}

	return true;
    }
}
