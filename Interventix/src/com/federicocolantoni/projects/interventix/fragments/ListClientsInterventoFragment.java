package com.federicocolantoni.projects.interventix.fragments;

import java.util.ArrayList;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
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
import com.federicocolantoni.projects.interventix.adapters.ListClientiAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.models.Cliente;
import com.federicocolantoni.projects.interventix.models.InterventoController;
import com.j256.ormlite.dao.RuntimeExceptionDao;

@SuppressLint({
	"InlinedApi", "NewApi"
})
@EFragment(R.layout.fragment_list_customers)
public class ListClientsInterventoFragment extends Fragment {

    @ViewById(R.id.search_client)
    EditText searchClient;

    @ViewById(R.id.list_clients)
    ListView listClienti;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Cliente.class)
    RuntimeExceptionDao<Cliente, Long> clienteDao;

    private ListClientiAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);
    };

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

		    mAdapter.selectItem(position);

		    Cliente clChecked = clienteDao.queryForSameId(mAdapter.getItem(position));

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

		    ListClientiAdapter.clickedItems.put(position, true);

		    for (int cont = 0; cont < ListClientiAdapter.clickedItems.size(); cont++) {

			if (cont != position)
			    ListClientiAdapter.clickedItems.put(cont, false);
		    }

		    mAdapter.selectItem(position);

		    mAdapter.notifyDataSetChanged();
		}
		else {
		    modClient.setVisibility(View.INVISIBLE);
		    ListClientiAdapter.swipedItems.put(position, false);
		    ListClientiAdapter.blockClick = false;

		    for (int cont = 0; cont < ListClientiAdapter.clickedItems.size(); cont++) {

			ListClientiAdapter.clickedItems.put(cont, true);
		    }

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

    @Override
    public void onResume() {

	super.onResume();

	InterventoController.listaClienti = (ArrayList<Cliente>) clienteDao.queryForAll();

    }
}
