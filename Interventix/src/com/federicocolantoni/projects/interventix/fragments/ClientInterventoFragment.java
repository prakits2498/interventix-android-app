package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.EFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;

@EFragment(R.layout.client_detail_fragment)
public class ClientInterventoFragment extends Fragment {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
	super.onCreate(savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
    }
    
    @Override
    public void onStart() {
    
	super.onStart();
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().getNumeroIntervento() + " - " + getString(R.string.row_client));
    }
}
