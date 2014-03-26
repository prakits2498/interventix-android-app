package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.models.InterventoController;

@EFragment(R.layout.fragment_annotations)
public class AnnotationsInterventoFragment extends Fragment {

    @StringRes(R.string.numero_intervento)
    String numeroIntervento;

    @StringRes(R.string.row_annotations)
    String rowAnnotations;

    @StringRes(R.string.new_interv)
    String nuovoIntervento;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {

	super.onStart();

	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(numeroIntervento + InterventoController.controller.getIntervento().numero + " - " + rowAnnotations);
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(nuovoIntervento + " - " + rowAnnotations);
    }

    @Override
    public void onResume() {

	super.onResume();

	updateUI();
    }

    private void updateUI() {

    }
}
