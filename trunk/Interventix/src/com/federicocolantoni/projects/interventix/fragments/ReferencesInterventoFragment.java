package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

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
import com.federicocolantoni.projects.interventix.models.InterventoController;

@EFragment(R.layout.fragment_references)
public class ReferencesInterventoFragment extends Fragment {

    @ViewById(R.id.row_rif_fattura)
    LinearLayout rifFattura;

    @ViewById(R.id.tv_row_rif_fattura)
    TextView tvRifFattura;

    @ViewById(R.id.row_rif_scontrino)
    LinearLayout rifScontrino;

    @ViewById(R.id.tv_row_rif_scontrino)
    TextView tvRifScontrino;

    @StringRes(R.string.numero_intervento)
    String numeroIntervento;

    @StringRes(R.string.row_references)
    String rowReferences;

    @StringRes(R.string.new_interv)
    String nuovoIntervento;

    @StringRes(R.string.riferimenti_fattura_title)
    String riferimentiFatturaTitle;

    @StringRes(R.string.riferimenti_scontrino_title)
    String riferimentiScontrinoTitle;

    @StringRes(R.string.ok_btn)
    String btnOk;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {

	super.onStart();

	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(numeroIntervento + InterventoController.controller.getIntervento().numero + " - " + rowReferences);
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(nuovoIntervento + " - " + rowReferences);
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

	rifFatturaDialog.setTitle(riferimentiFatturaTitle);

	mEditRiferimentiFattura = new EditText(getActivity());
	mEditRiferimentiFattura.setText(tvRifFattura.getText());

	rifFatturaDialog.setView(mEditRiferimentiFattura);

	rifFatturaDialog.setPositiveButton(btnOk, new DialogInterface.OnClickListener() {

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

	rifScontrinoDialog.setTitle(riferimentiScontrinoTitle);

	mEditRiferimentiScontrino = new EditText(getActivity());
	mEditRiferimentiScontrino.setText(tvRifScontrino.getText());

	rifScontrinoDialog.setView(mEditRiferimentiScontrino);

	rifScontrinoDialog.setPositiveButton(btnOk, new DialogInterface.OnClickListener() {

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
