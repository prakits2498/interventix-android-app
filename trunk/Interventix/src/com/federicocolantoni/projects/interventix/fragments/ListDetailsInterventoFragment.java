package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListDettagliInterventiAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;

@SuppressLint("NewApi")
@EFragment(R.layout.details_intervento_fragment)
public class ListDetailsInterventoFragment extends Fragment implements LoaderCallbacks<Cursor> {
    
    private final static int MESSAGE_LOADER = 1;
    
    private long mId_intervento;
    
    private static final String[] PROJECTION = new String[] {
	    DettaglioInterventoDB.Fields._ID,
	    DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO,
	    DettaglioInterventoDB.Fields.TIPO,
	    DettaglioInterventoDB.Fields.OGGETTO
    };
    
    private static final String SELECTION = DettaglioInterventoDB.Fields.TYPE + " =? AND " + DettaglioInterventoDB.Fields.INTERVENTO + " =?";
    
    private String[] SELECTION_ARGS;
    
    private ListDettagliInterventiAdapter mAdapter;
    
    // retrieve views
    
    @ViewById(R.id.tv_details_intervention)
    TextView tv_details_intervento;
    
    @ViewById(R.id.list_details_intervento)
    ListView detailsList;
    
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
	
	mId_intervento = bundle.getLong(Constants.ID_INTERVENTO);
	
	SELECTION_ARGS = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + mId_intervento
	};
	
	tv_details_intervento.setText("Dettagli");
	
	mAdapter = new ListDettagliInterventiAdapter(getActivity(), null);
	
	detailsList.setAdapter(mAdapter);
	
	detailsList.setOnItemClickListener(new OnItemClickListener() {
	    
	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
	    
		Bundle bundle = new Bundle();
		
		Cursor cur = (Cursor) adapter.getItemAtPosition(position);
		
		bundle.putLong(Constants.ID_DETTAGLIO_INTERVENTO, cur.getInt(cur.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO)));
		bundle.putString(Constants.NUOVO_DETTAGLIO_INTERVENTO, Constants.DETTAGLIO_INTERVENTO_ESISTENTE);
		
		FragmentManager manager = ((ActionBarActivity) getActivity()).getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		DetailInterventoFragment_ dettInterv = new DetailInterventoFragment_();
		dettInterv.setArguments(bundle);
		
		transaction.replace(R.id.fragments_layout, dettInterv, Constants.INFO_DETAIL_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.INFO_DETAIL_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		transaction.commit();
	    }
	});
	
	detailsList.setOnItemLongClickListener(new OnItemLongClickListener() {
	    
	    @Override
	    public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
	    
		// aggiungere il menu contestuale per la cancellazione del
		// dettaglio
		
		return false;
	    }
	});
	
	getActivity().getSupportLoaderManager().initLoader(MESSAGE_LOADER, null, this);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    
	super.onCreateOptionsMenu(menu, inflater);
	
	inflater.inflate(R.menu.menu_view_intervento, menu);
	
	MenuItem itemAddDetail = menu.findItem(R.id.add_detail_interv);
	itemAddDetail.setVisible(true);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    
	switch (item.getItemId()) {
	
	    case R.id.add_detail_interv:
		
		FragmentManager manager = getActivity().getSupportFragmentManager();
		
		FragmentTransaction transaction = manager.beginTransaction();
		
		DetailInterventoFragment newDetail = new DetailInterventoFragment();
		
		Bundle bundle = new Bundle();
		// bundle.putLong(Constants.ID_DETTAGLIO_INTERVENTO, -1l);
		bundle.putString(Constants.NUOVO_DETTAGLIO_INTERVENTO, Constants.NUOVO_DETTAGLIO_INTERVENTO);
		bundle.putLong(Constants.ID_INTERVENTO, mId_intervento);
		
		newDetail.setArguments(bundle);
		
		transaction.replace(R.id.fragments_layout, newDetail, Constants.NEW_DETAIL_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.NEW_DETAIL_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		transaction.commit();
		
		break;
	    
	    case R.id.pay:
		
		InterventixToast.makeToast(getActivity(), "Saldare l'intervento?", Toast.LENGTH_SHORT);
		
		break;
	    
	    case R.id.send_mail:
		
		InterventixToast.makeToast(getActivity(), "Inviare email?", Toast.LENGTH_SHORT);
		
		break;
	    
	    case R.id.close:
		
		InterventixToast.makeToast(getActivity(), "Chiudere l'intervento?", Toast.LENGTH_SHORT);
		
		break;
	}
	
	return true;
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
    
	Loader<Cursor> loader = new CursorLoader(getActivity(), DettaglioInterventoDB.CONTENT_URI, ListDetailsInterventoFragment.PROJECTION, ListDetailsInterventoFragment.SELECTION, SELECTION_ARGS, null);
	
	return loader;
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    
	mAdapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    
	mAdapter.swapCursor(null);
    }
}
