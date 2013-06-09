
package com.federicocolantoni.projects.interventix.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Cliente;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class OverViewInterventoFragment extends SherlockFragment implements
	OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);

	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
		true);

	Bundle bundle = getArguments();

	final View view = inflater.inflate(
		R.layout.overview_intervento_fragment, container, false);

	Intervento interv = null;

	interv = (Intervento) bundle.getSerializable("INTERVENTO");

	getSherlockActivity().getSupportActionBar().setSubtitle(
		"Intervento " + interv.getmIdIntervento());

	TextView summary = (TextView) view
		.findViewById(R.id.tv_summary_intervention);
	new DateFormat();
	summary.setText("Interv. " + interv.getmIdIntervento() + " del "
		+ DateFormat.format("dd/MM/yyyy", interv.getmDataOra()) + " "
		+ DateFormat.format("hh:mm", interv.getmDataOra()));

	View rowCliente = view.findViewById(R.id.row_client);

	TextView tv_row_client = (TextView) rowCliente
		.findViewById(R.id.tv_row_client);

	Cliente cliente = (Cliente) bundle.getSerializable("CLIENTE");
	tv_row_client.setText(cliente.getmNominativo());

	View rowTecnico = view.findViewById(R.id.row_user);

	TextView tv_row_user = (TextView) rowTecnico
		.findViewById(R.id.tv_row_user);
	tv_row_user.setText(bundle.getString("NOMINATIVO"));

	View rowInformazioni = view.findViewById(R.id.row_informations);
	rowInformazioni.setOnClickListener(this);

	return view;
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	    case R.id.row_informations:

		FragmentManager manager = getActivity()
			.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		InformationsInterventoFragment infoInterv = new InformationsInterventoFragment();

		infoInterv.setArguments(getArguments());

		transaction.replace(R.id.fragments_layout, infoInterv,
			Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
		transaction
			.addToBackStack(Constants.VIEW_INTERVENTO_TRANSACTION);
		transaction.commit();

		break;
	}
    }
}
