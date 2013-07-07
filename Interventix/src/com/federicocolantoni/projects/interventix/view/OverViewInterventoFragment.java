
package com.federicocolantoni.projects.interventix.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Cliente;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.utils.GetNominativoCliente;
import com.federicocolantoni.projects.interventix.utils.GetOverviewIntervento;

@SuppressLint("NewApi")
public class OverViewInterventoFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	BugSenseHandler.initAndStartSession(getSherlockActivity(),
		Constants.API_KEY);

	super.onCreateView(inflater, container, savedInstanceState);

	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
		true);

	Bundle bundle = getArguments();

	final View view = inflater.inflate(
		R.layout.overview_intervento_fragment, container, false);

	Intervento interv = null;

	try {
	    interv = new GetOverviewIntervento(getSherlockActivity()).execute(
		    bundle.getLong(Constants.ID_INTERVENTO)).get();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	} catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}

	getSherlockActivity().getSupportActionBar().setSubtitle(
		"Intervento " + bundle.getLong(Constants.ID_INTERVENTO));

	TextView summary = (TextView) view
		.findViewById(R.id.tv_summary_intervention);
	new DateFormat();
	summary.setText("Interv. "
		+ bundle.getLong(Constants.ID_INTERVENTO)
		+ " del "
		+ new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALY)
			.format(new Date(interv.getmDataOra())));

	View rowCliente = view.findViewById(R.id.row_client);

	TextView tv_row_client = (TextView) rowCliente
		.findViewById(R.id.tv_row_client);

	Cliente cliente = null;
	try {
	    cliente = new GetNominativoCliente(getSherlockActivity()).execute(
		    interv.getmIdCliente()).get();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}
	tv_row_client.setText(cliente.getmNominativo());

	View rowTecnico = view.findViewById(R.id.row_user);
	rowTecnico.setEnabled(false);

	TextView tv_row_tecnico = (TextView) rowTecnico
		.findViewById(R.id.tv_row_user);
	tv_row_tecnico.setText(bundle.getString(Constants.USER_NOMINATIVO));
	tv_row_tecnico.setBackgroundColor(Color.LTGRAY);
	tv_row_tecnico.setEnabled(false);

	final Bundle infoIntervBundle = new Bundle();
	infoIntervBundle.putAll(bundle);
	infoIntervBundle.putSerializable(Constants.CLIENTE, cliente);

	View rowInformazioni = view.findViewById(R.id.row_informations);
	//	rowInformazioni.setLayoutParams(rowTecnico.getLayoutParams());
	rowInformazioni.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		FragmentManager manager = getActivity()
			.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		InformationsInterventoFragment infoInterv = new InformationsInterventoFragment();

		infoInterv.setArguments(infoIntervBundle);

		transaction.replace(R.id.fragments_layout, infoInterv,
			Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
		transaction
			.addToBackStack(Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
		transaction
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		transaction.commit();
	    }
	});

	View rowDetails = view.findViewById(R.id.row_details);
	rowDetails.setLayoutParams(rowTecnico.getLayoutParams());

	View rowCosts = view.findViewById(R.id.row_costs);
	rowCosts.setLayoutParams(rowTecnico.getLayoutParams());

	View rowSignature = view.findViewById(R.id.row_signature);
	rowSignature.setLayoutParams(rowTecnico.getLayoutParams());

	return view;
    }
}
