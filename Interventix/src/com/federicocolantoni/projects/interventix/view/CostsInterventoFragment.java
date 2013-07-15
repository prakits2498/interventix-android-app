
package com.federicocolantoni.projects.interventix.view;

import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.utils.GetCostsIntervento;

public class CostsInterventoFragment extends SherlockFragment {

    public static long id_intervento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	BugSenseHandler.initAndStartSession(getSherlockActivity(),
		Constants.API_KEY);

	super.onCreateView(inflater, container, savedInstanceState);

	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
		true);

	final View view = inflater.inflate(R.layout.costs_intervento_fragment,
		container, false);

	Bundle bundle = getArguments();

	id_intervento = bundle.getLong(Constants.ID_INTERVENTO);

	TextView tv_costs_intervento = (TextView) view
		.findViewById(R.id.tv_costs_intervention);
	tv_costs_intervento.setText("Costi Intervento " + id_intervento);

	Intervento interv = null;

	try {
	    interv = new GetCostsIntervento(getSherlockActivity()).execute(
		    id_intervento).get();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	} catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}

	View rowManodopera = view.findViewById(R.id.row_manodopera);
	rowManodopera.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

	    }
	});

	TextView tv_row_manodopera = (TextView) rowManodopera
		.findViewById(R.id.tv_row_manodopera);
	tv_row_manodopera.setText(interv.getmCostoManodopera().toPlainString()
		+ " €");

	View rowComponenti = view.findViewById(R.id.row_componenti);
	rowComponenti.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

	    }
	});

	TextView tv_row_componenti = (TextView) rowComponenti
		.findViewById(R.id.tv_row_componenti);
	tv_row_componenti.setText(interv.getmCostoComponenti().toPlainString()
		+ " €");

	View rowAccessori = view.findViewById(R.id.row_accessori);
	rowAccessori.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

	    }
	});

	TextView tv_row_accessori = (TextView) rowAccessori
		.findViewById(R.id.tv_row_accessori);
	tv_row_accessori.setText(interv.getmCostoAccessori().toPlainString()
		+ " €");

	View rowImporto = view.findViewById(R.id.row_importo);

	TextView tv_row_importo = (TextView) rowImporto
		.findViewById(R.id.tv_row_importo);
	tv_row_importo.setText(interv.getmImporto().toPlainString() + " €");

	View rowIVA = view.findViewById(R.id.row_iva);

	TextView tv_row_iva = (TextView) rowIVA.findViewById(R.id.tv_row_iva);
	tv_row_iva.setText(interv.getmIva().toPlainString() + " €");

	View rowTotale = view.findViewById(R.id.row_totale);

	TextView tv_row_totale = (TextView) rowTotale
		.findViewById(R.id.tv_row_totale);
	tv_row_totale.setText(interv.getmTotale().toPlainString() + " €");

	return view;
    }
}
