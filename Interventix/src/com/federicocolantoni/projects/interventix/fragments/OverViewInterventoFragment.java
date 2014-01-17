package com.federicocolantoni.projects.interventix.fragments;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;

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
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.Interventix;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.task.GetListaDettagliInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetNominativoClienteAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetOverviewInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.ListDetailsIntervento;

@SuppressLint("NewApi")
@EFragment(R.layout.overview_intervento_fragment)
public class OverViewInterventoFragment extends Fragment {
    
    // retrieve views
    @ViewById(R.id.tv_summary_intervention)
    TextView summary;
    
    @ViewById(R.id.row_client)
    View rowCliente;
    
    @ViewById(R.id.row_user)
    View rowTecnico;
    
    @ViewById(R.id.row_informations)
    View rowInformazioni;
    
    @ViewById(R.id.row_details)
    View rowDetails;
    
    @ViewById(R.id.row_costs)
    View rowCosts;
    
    @ViewById(R.id.row_signature)
    View rowSignature;
    
    // *** variabili per il nuovo intervento - start ***\\
    private static final String CLIENTE_NUOVO_INTERVENTO = "descrizione";
    private static final String FIRMA_NUOVO_INTERVENTO = "oggetto";
    private static final String COSTI_NUOVO_INTERVENTO = "tipo";
    private static final String INFORMAZIONI_NUOVO_INTERVENTO = "fine";
    
    private static JSONObject sNewIntervento;
    // *** variabili per il nuovo intervento - end ***\\
    
    FragmentManager manager;
    
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
	
	Bundle bundle = getArguments();
	
	Intervento interv = null;
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("Intervento " + bundle.getLong(Constants.NUMERO_INTERVENTO));
	
	manager = ((ActionBarActivity) getActivity()).getSupportFragmentManager();
	
	if (bundle.getLong(Constants.ID_INTERVENTO) > -1l) {
	    
	    final Bundle intervIDBundle = new Bundle();
	    intervIDBundle.putAll(bundle);
	    
	    try {
		interv = new GetOverviewInterventoAsyncTask(Interventix.getContext()).execute(bundle.getLong(Constants.ID_INTERVENTO)).get();
	    }
	    catch (InterruptedException e) {
		
		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }
	    catch (ExecutionException e) {
		
		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }
	    
	    DateTime dt_interv = new DateTime(interv.getDataOra(), DateTimeZone.forID("Europe/Rome"));
	    
	    summary.setText("Interv. " + bundle.getLong(Constants.NUMERO_INTERVENTO) + " del " + dt_interv.toString("dd/MM/yyyy HH:mm"));
	    
	    rowCliente.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		
		    FragmentTransaction transaction = manager.beginTransaction();
		    
		    ListClientsInterventoFragment_ clientiInterv = new ListClientsInterventoFragment_();
		    clientiInterv.setArguments(intervIDBundle);
		    
		    transaction.detach(OverViewInterventoFragment.this);
		    transaction.replace(R.id.fragments_layout, clientiInterv, Constants.CLIENTS_INTERVENTO_FRAGMENT);
		    transaction.addToBackStack(Constants.CLIENTS_INTERVENTO_FRAGMENT);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    
		    transaction.commit();
		}
	    });
	    
	    TextView tv_row_client = (TextView) rowCliente.findViewById(R.id.tv_row_client);
	    
	    Cliente cliente = null;
	    
	    try {
		cliente = new GetNominativoClienteAsyncTask(Interventix.getContext()).execute(interv.getIdCliente()).get();
	    }
	    catch (InterruptedException e) {
		
		BugSenseHandler.sendException(e);
		e.printStackTrace();
	    }
	    catch (ExecutionException e) {
		
		BugSenseHandler.sendException(e);
		e.printStackTrace();
	    }
	    
	    tv_row_client.setText(cliente.getNominativo());
	    
	    rowTecnico.setEnabled(false);
	    
	    TextView tv_row_tecnico = (TextView) rowTecnico.findViewById(R.id.tv_row_user);
	    tv_row_tecnico.setText(bundle.getString(Constants.USER_NOMINATIVO));
	    tv_row_tecnico.setBackgroundColor(Color.LTGRAY);
	    tv_row_tecnico.setEnabled(false);
	    
	    rowInformazioni.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		
		    FragmentTransaction transaction = manager.beginTransaction();
		    
		    InformationsInterventoFragment_ infoInterv = new InformationsInterventoFragment_();
		    infoInterv.setArguments(intervIDBundle);
		    
		    transaction.detach(OverViewInterventoFragment.this);
		    transaction.replace(R.id.fragments_layout, infoInterv, Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
		    transaction.addToBackStack(Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    
		    transaction.commit();
		}
	    });
	    
	    ListDetailsIntervento listaDetailsInterv = null;
	    
	    try {
		
		listaDetailsInterv = new GetListaDettagliInterventoAsyncTask(Interventix.getContext()).execute(bundle.getLong(Constants.ID_INTERVENTO)).get();
	    }
	    catch (InterruptedException e) {
		
		BugSenseHandler.sendException(e);
		e.printStackTrace();
	    }
	    catch (ExecutionException e) {
		
		BugSenseHandler.sendException(e);
		e.printStackTrace();
	    }
	    
	    rowDetails.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		
		    FragmentTransaction transaction = manager.beginTransaction();
		    
		    ListDetailsInterventoFragment_ detailsInterv = new ListDetailsInterventoFragment_();
		    
		    Bundle detailsIntervBundle = new Bundle(intervIDBundle);
		    
		    detailsInterv.setArguments(detailsIntervBundle);
		    
		    transaction.detach(OverViewInterventoFragment.this);
		    transaction.replace(R.id.fragments_layout, detailsInterv, Constants.DETAILS_INTERVENTO_FRAGMENT);
		    transaction.addToBackStack(Constants.DETAILS_INTERVENTO_FRAGMENT);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    
		    transaction.commit();
		}
	    });
	    
	    TextView tv_row_details = (TextView) rowDetails.findViewById(R.id.tv_row_details);
	    
	    long numOre = 0l;
	    
	    for (DettaglioIntervento obj : listaDetailsInterv.getListDetails()) {
		
		numOre += obj.getFine() - obj.getInizio();
	    }
	    
	    DateTime dt = new DateTime(numOre, DateTimeZone.forID("Europe/Rome"));
	    
	    if (listaDetailsInterv.getListDetails().size() > 0) {
		tv_row_details.setText(listaDetailsInterv.getListDetails().size() + " - " + dt.toString("HH:mm"));
	    }
	    else {
		tv_row_details.setText(getString(R.string.no_details_interv));
	    }
	    
	    rowCosts.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		
		    FragmentTransaction transaction = manager.beginTransaction();
		    
		    CostsInterventoFragment_ costsInterv = new CostsInterventoFragment_();
		    costsInterv.setArguments(intervIDBundle);
		    
		    transaction.detach(OverViewInterventoFragment.this);
		    transaction.replace(R.id.fragments_layout, costsInterv, Constants.COSTS_INTERVENTO_FRAGMENT);
		    transaction.addToBackStack(Constants.COSTS_INTERVENTO_FRAGMENT);
		    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		    
		    transaction.commit();
		}
	    });
	    
	    DecimalFormat formatter = new DecimalFormat("###,###,###.##");
	    
	    TextView tv_row_costs = (TextView) rowCosts.findViewById(R.id.tv_row_costs);
	    tv_row_costs.setText(formatter.format(interv.getTotale().doubleValue()) + " €");
	    
	    TextView tv_row_signature = (TextView) rowSignature.findViewById(R.id.tv_row_signature);
	    if (interv.getFirma().length() > 0) {
		tv_row_signature.setText("Presente");
		rowSignature.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
		    
			FragmentTransaction transaction = manager.beginTransaction();
			
			SignatureInterventoFragment_ signInterv = new SignatureInterventoFragment_();
			signInterv.setArguments(intervIDBundle);
			
			transaction.detach(OverViewInterventoFragment.this);
			transaction.replace(R.id.fragments_layout, signInterv, Constants.SIGNATURE_INTERVENTO_FRAGMENT);
			transaction.addToBackStack(Constants.SIGNATURE_INTERVENTO_FRAGMENT);
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			
			transaction.commit();
		    }
		});
	    }
	    else {
		tv_row_signature.setText("Non presente");
	    }
	}
	else {
	    
	    addNewIntervento(bundle);
	}
    }
    
    private void addNewIntervento(Bundle bundle) {
    
	((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("Intervento " + bundle.getLong(Constants.NUMERO_INTERVENTO));
	
	manager = ((ActionBarActivity) getActivity()).getSupportFragmentManager();
	
	Intervento interv = null;
	
	final Bundle intervIDBundle = new Bundle();
	intervIDBundle.putAll(bundle);
	
	try {
	    interv = new GetOverviewInterventoAsyncTask(Interventix.getContext()).execute(bundle.getLong(Constants.ID_INTERVENTO)).get();
	}
	catch (InterruptedException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	DateTime dt_interv = new DateTime();
	
	summary.setText("Nuovo Interv. " + bundle.getLong(Constants.NUMERO_INTERVENTO) + " del " + dt_interv.toString("dd/MM/yyyy HH:mm"));
	
	rowCliente.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
	    
		FragmentTransaction transaction = manager.beginTransaction();
		
		ListClientsInterventoFragment clientiInterv = new com.federicocolantoni.projects.interventix.fragments.ListClientsInterventoFragment_();
		clientiInterv.setArguments(intervIDBundle);
		
		transaction.detach(OverViewInterventoFragment.this);
		transaction.replace(R.id.fragments_layout, clientiInterv, Constants.CLIENTS_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.CLIENTS_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		transaction.commit();
	    }
	});
	
	TextView tv_row_client = (TextView) rowCliente.findViewById(R.id.tv_row_client);
	
	Cliente cliente = null;
	
	try {
	    cliente = new GetNominativoClienteAsyncTask(Interventix.getContext()).execute(interv.getIdCliente()).get();
	}
	catch (InterruptedException e) {
	    
	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	catch (ExecutionException e) {
	    
	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	
	tv_row_client.setText(cliente.getNominativo() != null ? cliente.getNominativo() : "");
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    
	super.onCreateOptionsMenu(menu, inflater);
	
	inflater.inflate(R.menu.menu_view_intervento, menu);
	
	MenuItem itemAddDetail = menu.findItem(R.id.add_detail_interv);
	itemAddDetail.setEnabled(false);
	itemAddDetail.setVisible(true);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    
	switch (item.getItemId()) {
	    case R.id.pay:
		
		InterventixToast.makeToast(Interventix.getContext(), "Saldare l'intervento?", Toast.LENGTH_SHORT);
		
		break;
	    
	    case R.id.send_mail:
		
		InterventixToast.makeToast(Interventix.getContext(), "Inviare email?", Toast.LENGTH_SHORT);
		
		break;
	    
	    case R.id.close:
		
		InterventixToast.makeToast(Interventix.getContext(), "Chiudere l'intervento?", Toast.LENGTH_SHORT);
		
		break;
	}
	
	return true;
    }
}
