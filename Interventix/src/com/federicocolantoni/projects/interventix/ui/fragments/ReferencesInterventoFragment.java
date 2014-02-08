package com.federicocolantoni.projects.interventix.ui.fragments;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;

@EFragment(R.layout.references_fragment)
public class ReferencesInterventoFragment extends Fragment {
    
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
	
	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.row_references));
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_references));
    }
    
    @Override
    public void onResume() {
    
	super.onResume();
	
	updateUI();
    }
    
    private void updateUI() {
    
	tvRifFattura.setText(InterventoController.controller.getIntervento().riffattura);
	tvRifScontrino.setText(InterventoController.controller.getIntervento().rifscontrino);
    }
    
    @Click(R.id.row_rif_fattura)
    void showDialogRifFattura() {
    
	final EditText mEditRiferimentiFattura;
	
	AlertDialog.Builder rifFatturaDialog = new Builder(getActivity());
	
	rifFatturaDialog.setTitle(R.string.riferimenti_fattura_title);
	
	TextView tv_motivo = (TextView) getActivity().findViewById(R.id.tv_row_rif_fattura);
	
	mEditRiferimentiFattura = new EditText(getActivity());
	mEditRiferimentiFattura.setText(tv_motivo.getText());
	
	rifFatturaDialog.setView(mEditRiferimentiFattura);
	
	rifFatturaDialog.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		dialog.dismiss();
		
		InterventoController.controller.getIntervento().riffattura = (mEditRiferimentiFattura.getText().toString());
		
		updateUI();
	    }
	});
	
	rifFatturaDialog.create().show();
    }
    
    @Click(R.id.row_rif_scontrino)
    void showDialogRifScontrino() {
    
	final EditText mEditRiferimentiScontrino;
	
	AlertDialog.Builder rifScontrinoDialog = new Builder(getActivity());
	
	rifScontrinoDialog.setTitle(R.string.riferimenti_scontrino_title);
	
	TextView tv_motivo = (TextView) getActivity().findViewById(R.id.tv_row_rif_scontrino);
	
	mEditRiferimentiScontrino = new EditText(getActivity());
	mEditRiferimentiScontrino.setText(tv_motivo.getText());
	
	rifScontrinoDialog.setView(mEditRiferimentiScontrino);
	
	rifScontrinoDialog.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		dialog.dismiss();
		
		InterventoController.controller.getIntervento().rifscontrino = (mEditRiferimentiScontrino.getText().toString());
		
		updateUI();
	    }
	});
	
	rifScontrinoDialog.create().show();
    }
}
