package com.federicocolantoni.projects.interventix.fragments;

import java.text.DecimalFormat;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.models.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.models.InterventoController;
import com.federicocolantoni.projects.interventix.models.UtenteController;

@SuppressLint("NewApi")
@EFragment(R.layout.fragment_overview)
public class OverViewInterventoFragment extends Fragment {

    @ViewById(R.id.tv_row_client)
    TextView tvRowClient;

    @ViewById(R.id.row_user)
    View rowTecnico;

    @ViewById(R.id.tv_row_user)
    TextView tvRowTecnico;

    @ViewById(R.id.tv_row_informations)
    TextView tvRowInformazioni;

    @ViewById(R.id.tv_row_details)
    TextView tvRowDetails;

    @ViewById(R.id.tv_row_costs)
    TextView tvRowCosts;

    @ViewById(R.id.tv_row_signature)
    TextView tvRowSignature;

    private FragmentManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {

	super.onStart();

	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero);
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv));

	manager = ((ActionBarActivity) getActivity()).getSupportFragmentManager();

	// *** nuova gestione - inizio ***\\

	if (InterventoController.controller != null) {

	    rowTecnico.setEnabled(false);
	}
	else {

	    // gestione creazione nuovo intervento

	    addNewIntervento();
	}

	// *** nuova gestione - fine ***\\
    }

    @Override
    public void onResume() {

	super.onResume();

	updateUI();
    }

    private void addNewIntervento() {

	manager = ((ActionBarActivity) getActivity()).getSupportFragmentManager();

	((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("Nuovo intervento");
    }

    private void updateUI() {

	tvRowClient.setText(InterventoController.controller.getCliente().nominativo);

	tvRowTecnico.setText(UtenteController.tecnicoLoggato.nome + " " + UtenteController.tecnicoLoggato.cognome);
	tvRowTecnico.setBackgroundColor(Color.LTGRAY);
	tvRowTecnico.setEnabled(false);

	rowTecnico.setEnabled(false);

	DateTime dt = new DateTime(InterventoController.controller.getIntervento().dataora, DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	tvRowInformazioni.setText(dt.toString(Constants.DATE_PATTERN + " " + Constants.TIME_PATTERN));

	long numOre = 0l;

	for (DettaglioIntervento obj : InterventoController.controller.getListaDettagli()) {

	    numOre += obj.fine - obj.inizio;
	}

	DateTime dtDetails = new DateTime(numOre, DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	if (InterventoController.controller.getListaDettagli().size() > 0) {

	    tvRowDetails.setText(InterventoController.controller.getListaDettagli().size() + " - " + dtDetails.toString(Constants.TIME_PATTERN));
	}
	else {

	    tvRowDetails.setText(getString(R.string.no_details));
	}

	DecimalFormat formatter = new DecimalFormat("###,###,###.##");

	if (InterventoController.controller.getIntervento().totale != null)
	    tvRowCosts.setText(formatter.format(InterventoController.controller.getIntervento().totale.doubleValue()) + " €");

	if (InterventoController.controller.getIntervento().firma != null)
	    if (InterventoController.controller.getIntervento().firma.length() > 0) {

		tvRowSignature.setText("Presente");
	    }
	    else {

		tvRowSignature.setText("Non presente");
	    }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	super.onCreateOptionsMenu(menu, inflater);

	inflater.inflate(R.menu.menu_view_intervento, menu);

	MenuItem itemAddDetail = menu.findItem(R.id.add_detail_interv);
	itemAddDetail.setEnabled(false);
	itemAddDetail.setIcon(R.drawable.ic_action_add_disabled);
	itemAddDetail.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {
	    case R.id.pay:

		break;

	    case R.id.send_mail:

		break;

	    case R.id.close:

		break;
	}

	return true;
    }

    @Click(R.id.row_client)
    void showListaClientiFragment() {

	FragmentTransaction transaction = manager.beginTransaction();

	ListClientsInterventoFragment_ clientiInterv = new ListClientsInterventoFragment_();

	transaction.replace(R.id.fragments_layout, clientiInterv, Constants.CLIENTS_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.CLIENTS_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

	transaction.commit();
    }

    @Click(R.id.row_informations)
    void showInformazioniFragment() {

	FragmentTransaction transaction = manager.beginTransaction();

	InformationsInterventoFragment_ infoInterv = new InformationsInterventoFragment_();

	transaction.replace(R.id.fragments_layout, infoInterv, Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

	transaction.commit();
    }

    @Click(R.id.row_details)
    void showListaDettagliFragment() {

	FragmentTransaction transaction = manager.beginTransaction();

	ListDetailsInterventoFragment_ detailsInterv = new ListDetailsInterventoFragment_();

	transaction.replace(R.id.fragments_layout, detailsInterv, Constants.DETAILS_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.DETAILS_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

	transaction.commit();
    }

    @Click(R.id.row_costs)
    void showCostiFragment() {

	FragmentTransaction transaction = manager.beginTransaction();

	CostsInterventoFragment_ costsInterv = new CostsInterventoFragment_();

	transaction.replace(R.id.fragments_layout, costsInterv, Constants.COSTS_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.COSTS_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

	transaction.commit();
    }

    @Click(R.id.row_signature)
    void showFirmaFragment() {

	FragmentTransaction transaction = manager.beginTransaction();

	SignatureInterventoFragment_ signInterv = new SignatureInterventoFragment_();

	transaction.replace(R.id.fragments_layout, signInterv, Constants.SIGNATURE_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.SIGNATURE_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

	transaction.commit();
    }
}
