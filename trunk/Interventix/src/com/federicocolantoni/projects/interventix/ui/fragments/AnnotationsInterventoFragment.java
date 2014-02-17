package com.federicocolantoni.projects.interventix.ui.fragments;

import org.androidannotations.annotations.EFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;

@EFragment(R.layout.fragment_annotations)
public class AnnotationsInterventoFragment extends Fragment {

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
					getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.row_annotations));
		else
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_annotations));
	}

	@Override
	public void onResume() {

		super.onResume();

		updateUI();
	}

	private void updateUI() {

	}
}
