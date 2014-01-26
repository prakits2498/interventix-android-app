package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;

@EFragment(R.layout.riferimenti_fragment)
public class ReferencesFragment extends Fragment {
    
    @ViewById(R.id.row_rif_fattura)
    LinearLayout rifFattura;
    
    @ViewById(R.id.tv_row_rif_fattura)
    TextView tvRifFattura;
    
    @ViewById(R.id.row_rif_scontrino)
    LinearLayout rifScontrino;
    
    @ViewById(R.id.tv_row_rif_scontrino)
    TextView tvRifScontrino;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
	super.onCreate(savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setHasOptionsMenu(true);
    }
    
    @Override
    public void onStart() {
    
	super.onStart();
	
	if (!InterventoController.controller.getIntervento().isNuovo())
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().getNumeroIntervento() + " - " + getString(R.string.row_references));
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("Nuovo intervento - " + getString(R.string.row_references));
    }
    
    @Override
    public void onResume() {
    
	super.onResume();
	
	updateUI();
    }
    
    private void updateUI() {
    
    }
    
    @Click(R.id.row_rif_fattura)
    void showDialogRifFattura() {
    
    }
    
    @Click(R.id.row_rif_scontrino)
    void showDialogRifScontrino() {
    
    }
}
