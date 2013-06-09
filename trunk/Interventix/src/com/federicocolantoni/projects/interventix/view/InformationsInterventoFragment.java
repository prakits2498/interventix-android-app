
package com.federicocolantoni.projects.interventix.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

public class InformationsInterventoFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);

	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
		true);

	final View view = inflater.inflate(
		R.layout.information_intervento_fragment, container, false);

	Bundle bundle = getArguments();

	Intervento interv = (Intervento) bundle.getSerializable("INTERVENTO");

	TextView info_interv = (TextView) view
		.findViewById(R.id.tv_info_intervention);
	info_interv.setText("Informazioni Intervento "
		+ interv.getmIdIntervento());

	return view;
    }
}
