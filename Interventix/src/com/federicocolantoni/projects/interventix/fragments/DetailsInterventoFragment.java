package com.federicocolantoni.projects.interventix.fragments;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.DettaglioInterventoAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;

@SuppressLint("NewApi")
public class DetailsInterventoFragment extends Fragment implements LoaderCallbacks<Cursor> {
    
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
    
    private DettaglioInterventoAdapter mAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setHasOptionsMenu(true);
	
	final View view = inflater.inflate(R.layout.details_intervento_fragment, container, false);
	
	Bundle bundle = getArguments();
	
	mId_intervento = bundle.getLong(Constants.ID_INTERVENTO);
	
	System.out.println("LISTA DETTAGLI - ID INTERVENTO " + mId_intervento);
	
	SELECTION_ARGS = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + mId_intervento
	};
	
	TextView tv_costs_intervento = (TextView) view.findViewById(R.id.tv_details_intervention);
	tv_costs_intervento.setText("Dettagli Intervento " + bundle.getLong(Constants.NUMERO_INTERVENTO));
	
	ListView detailsList = (ListView) view.findViewById(R.id.list_details_intervento);
	
	mAdapter = new DettaglioInterventoAdapter(getActivity(), null);
	
	detailsList.setAdapter(mAdapter);
	
	detailsList.setOnItemClickListener(new OnItemClickListener() {
	    
	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		
		Bundle bundle = new Bundle();
		
		Cursor cur = (Cursor) adapter.getItemAtPosition(position);
		
		bundle.putLong(Constants.ID_DETTAGLIO_INTERVENTO, cur.getInt(cur.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO)));
		
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		DetailInterventoFragment dettInterv = new DetailInterventoFragment();
		dettInterv.setArguments(bundle);
		
		transaction.replace(R.id.fragments_layout, dettInterv, Constants.INFO_DETAIL_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.INFO_DETAIL_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		transaction.commit();
	    }
	});
	
	getActivity().getSupportLoaderManager().initLoader(MESSAGE_LOADER, null, this);
	
	return view;
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
		bundle.putLong(Constants.ID_DETTAGLIO_INTERVENTO, -1l);
		
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
	
	Loader<Cursor> loader = new CursorLoader(getActivity(), DettaglioInterventoDB.CONTENT_URI, DetailsInterventoFragment.PROJECTION, DetailsInterventoFragment.SELECTION, SELECTION_ARGS, null);
	
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
