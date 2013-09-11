package com.federicocolantoni.projects.interventix.fragments;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.federicocolantoni.projects.interventix.intervento.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.task.GetDettagliInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetNominativoClienteAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetOverviewInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.ListDetailsIntervento;

@SuppressLint("NewApi")
public class OverViewInterventoFragment extends SherlockFragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	BugSenseHandler.initAndStartSession(getSherlockActivity(), Constants.API_KEY);
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	Bundle bundle = getArguments();
	
	final View view = inflater.inflate(R.layout.overview_intervento_fragment, container, false);
	
	Intervento interv = null;
	
	try {
	    interv = new GetOverviewInterventoAsyncTask(getSherlockActivity()).execute(bundle.getLong(Constants.ID_INTERVENTO)).get();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	getSherlockActivity().getSupportActionBar().setSubtitle("Intervento " + bundle.getLong(Constants.NUMERO_INTERVENTO));
	
	TextView summary = (TextView) view.findViewById(R.id.tv_summary_intervention);
	// new DateFormat();
	
	DateTime dt_interv = new DateTime(interv.getmDataOra(), DateTimeZone.forID("Europe/Rome"));
	
	summary.setText("Interv. " + bundle.getLong(Constants.NUMERO_INTERVENTO) + " del " + /*
											      * new
											      * SimpleDateFormat
											      * (
											      * "dd/MM/yyyy HH:mm"
											      * ,
											      * Locale
											      * .
											      * ITALY
											      * )
											      * .
											      * format
											      * (
											      * new
											      * Date
											      * (
											      * interv
											      * .
											      * getmDataOra
											      * (
											      * )
											      * )
											      * )
											      * +
											      */dt_interv.toString("dd/MM/yyyy HH:mm"));
	
	View rowCliente = view.findViewById(R.id.row_client);
	rowCliente.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
	    }
	});
	
	TextView tv_row_client = (TextView) rowCliente.findViewById(R.id.tv_row_client);
	
	Cliente cliente = null;
	
	try {
	    cliente = new GetNominativoClienteAsyncTask(getSherlockActivity()).execute(interv.getmIdCliente()).get();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	}
	catch (ExecutionException e) {
	    e.printStackTrace();
	}
	
	tv_row_client.setText(cliente.getmNominativo());
	
	View rowTecnico = view.findViewById(R.id.row_user);
	rowTecnico.setEnabled(false);
	
	TextView tv_row_tecnico = (TextView) rowTecnico.findViewById(R.id.tv_row_user);
	tv_row_tecnico.setText(bundle.getString(Constants.USER_NOMINATIVO));
	tv_row_tecnico.setBackgroundColor(Color.LTGRAY);
	tv_row_tecnico.setEnabled(false);
	
	final Bundle intervIDBundle = new Bundle();
	intervIDBundle.putAll(bundle);
	
	View rowInformazioni = view.findViewById(R.id.row_informations);
	rowInformazioni.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		FragmentManager manager = getSherlockActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		InformationsInterventoFragment infoInterv = new InformationsInterventoFragment();
		infoInterv.setArguments(intervIDBundle);
		
		transaction.replace(R.id.fragments_layout, infoInterv, Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		transaction.commit();
	    }
	});
	
	View rowDetails = view.findViewById(R.id.row_details);
	
	ListDetailsIntervento listaDetailsInterv = null;
	
	try {
	    listaDetailsInterv = new GetDettagliInterventoAsyncTask(getSherlockActivity()).execute(bundle.getLong(Constants.ID_INTERVENTO)).get();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	}
	catch (ExecutionException e) {
	    e.printStackTrace();
	}
	
	rowDetails.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		DetailsInterventoFragment detailsInterv = new DetailsInterventoFragment();
		
		Bundle detailsIntervBundle = new Bundle(intervIDBundle);
		
		detailsInterv.setArguments(detailsIntervBundle);
		
		transaction.replace(R.id.fragments_layout, detailsInterv, Constants.DETAILS_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.DETAILS_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		transaction.commit();
	    }
	});
	
	TextView tv_row_details = (TextView) rowDetails.findViewById(R.id.tv_row_details);
	
	long numOre = 0l;
	
	for (DettaglioIntervento obj : listaDetailsInterv.getListDetails()) {
	    
	    numOre += obj.getmFine() - obj.getmInizio();
	}
	
	DateTime dt = new DateTime(numOre, DateTimeZone.forID("Europe/Rome"));
	
	if (listaDetailsInterv.getListDetails().size() > 0) {
	    tv_row_details.setText(listaDetailsInterv.getListDetails().size() + " - " + dt.toString("HH:mm"));
	}
	else {
	    tv_row_details.setText(getString(R.string.no_details_interv));
	}
	
	View rowCosts = view.findViewById(R.id.row_costs);
	rowCosts.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		CostsInterventoFragment costsInterv = new CostsInterventoFragment();
		costsInterv.setArguments(intervIDBundle);
		
		transaction.replace(R.id.fragments_layout, costsInterv, Constants.COSTS_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.COSTS_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		transaction.commit();
	    }
	});
	
	DecimalFormat formatter = new DecimalFormat("###,###,###.##");
	
	TextView tv_row_costs = (TextView) rowCosts.findViewById(R.id.tv_row_costs);
	tv_row_costs.setText(formatter.format(interv.getmTotale().doubleValue()) + " €");
	
	View rowSignature = view.findViewById(R.id.row_signature);
	rowSignature.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
	    }
	});
	
	TextView tv_row_signature = (TextView) rowSignature.findViewById(R.id.tv_row_signature);
	if (interv.getmFirma() != null) {
	    tv_row_signature.setText("Presente");
	}
	else {
	    tv_row_signature.setText("Non presente");
	}
	
	return view;
    }
}
